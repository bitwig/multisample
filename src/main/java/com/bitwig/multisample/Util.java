package com.bitwig.multisample;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.SchemaOutputResolver;
import org.xml.sax.SAXException;

public class Util
{
   public static final String FORMAT_NAME = "DAW-project exchange format";
   public static final String FILE_EXTENSION = "multisample";

   private static final String PROJECT_FILE = "multisample.xml";

   public static void exportSchema(File file, Class cls) throws IOException
   {
      try
      {
         var context = createContext(cls);

         var resolver = new SchemaOutputResolver()
         {
            @Override public Result createOutput (String namespaceUri, String suggestedFileName) throws IOException
            {
               FileOutputStream fileOutputStream = new FileOutputStream(file);
               StreamResult result = new StreamResult(fileOutputStream);
               result.setSystemId(file.getName());
               return result;
            }
         };

         context.generateSchema(resolver);
      }
      catch (JAXBException e)
      {
         throw new IOException(e);
      }
   }

   private static String toXML(Object object) throws IOException
   {
      try
      {
         var context = createContext(object.getClass());

         var marshaller = context.createMarshaller();
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

         var sw = new StringWriter();
         marshaller.marshal(object, sw);

         return sw.toString();
      }
      catch (Exception e)
      {
         throw new IOException(e);
      }
   }

   private static JAXBContext createContext(final Class cls) throws JAXBException
   {
      return JAXBContext.newInstance(cls);
   }

   private static <T extends Object> T fromXML(InputStreamReader reader, Class<T> cls) throws IOException
   {
      try
      {
         var jaxbContext = JAXBContext.newInstance(cls);

         final var unmarshaller = jaxbContext.createUnmarshaller();

         final var object = (T)unmarshaller.unmarshal(reader);

         return object;
      }
      catch (JAXBException e)
      {
         throw new IOException(e);
      }
   }

   public static void saveXML(Multisample multisample, File file) throws IOException
   {
      String projectXML = toXML(multisample);
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(projectXML.getBytes(StandardCharsets.UTF_8));
      fileOutputStream.close();
   }

   public static void validate(Multisample multisample) throws IOException
   {
      String projectXML = toXML(multisample);

      try
      {
         var context = createContext(Multisample.class);

         final var schemaFile = File.createTempFile("schema", ".xml");
         exportSchema(schemaFile, Multisample.class);

         SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
         Schema schema = sf.newSchema(schemaFile);

         final var unmarshaller = context.createUnmarshaller();
         unmarshaller.setSchema(schema);

         unmarshaller.unmarshal(new StringReader(projectXML));
      }
      catch (JAXBException e)
      {
         throw new IOException(e);
      }
      catch (SAXException e)
      {
         throw new IOException(e);
      }
   }

   public static void save(Multisample multisample, Map<File, String> embeddedFiles, File file) throws IOException
   {
      String projectXML = toXML(multisample);

      final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
      zos.setMethod(ZipOutputStream.STORED);

      addUncompressedFileToZip(zos, PROJECT_FILE, projectXML.getBytes(StandardCharsets.UTF_8));

      for (Map.Entry<File, String> entry : embeddedFiles.entrySet())
      {
         addUncompressedFileToZip(zos, entry.getValue(), entry.getKey());
      }

      zos.close();
   }

   private static void addUncompressedFileToZip(
      final ZipOutputStream zos,
      final String pathInZip,
      final byte[] data) throws IOException
   {
      final ZipEntry entry = new ZipEntry(pathInZip);

      final CRC32 crc = new CRC32();
      crc.update(data);

      entry.setSize(data.length);
      entry.setCompressedSize(data.length);
      entry.setCrc(crc.getValue());
      entry.setMethod(ZipOutputStream.STORED);
      zos.putNextEntry(entry);
      zos.write(data);
      zos.closeEntry();
   }

   public static void addUncompressedFileToZip(
      final ZipOutputStream zos,
      final String pathInZip,
      final File file) throws IOException
   {
      final ZipEntry entry = new ZipEntry(pathInZip);

      final CRC32 crc = new CRC32();

      final byte[] buffer = new byte[1024];

      final InputStream input = new FileInputStream(file);
      long length = 0;

      try
      {
         while (true)
         {
            final int amountRead = input.read(buffer);

            assert amountRead > 0 || amountRead == -1;

            if (amountRead > 0)
            {
               crc.update(buffer, 0, amountRead);
               length += amountRead;
            }

            if (amountRead == -1)
            {
               break;
            }
         }
      }
      finally
      {
         try
         {
            input.close();
         }
         catch (final IOException e)
         {
         }
      }

      entry.setSize(length);
      entry.setCompressedSize(length);
      entry.setCrc(crc.getValue());
      entry.setMethod(ZipOutputStream.STORED);
      zos.putNextEntry(entry);

      final InputStream input2 = new FileInputStream(file);

      try
      {
         long lengthWritten = 0;

         while (true)
         {
            final int amountRead = input2.read(buffer);

            if (amountRead == -1)
            {
               break;
            }

            assert amountRead > 0;

            lengthWritten += amountRead;

            if (lengthWritten > length)
            {
               throw new IOException("File size changed while writing");
            }

            zos.write(buffer, 0, amountRead);
         }

         if (lengthWritten != length)
         {
            throw new IOException("File size changed while writing");
         }

         zos.closeEntry();
      }
      finally
      {
         try
         {
            input2.close();
         }
         catch (final IOException e)
         {
         }
      }
   }

   public static Multisample loadMultisample(final File file) throws IOException
   {
      ZipFile zipFile = new ZipFile(file);

      ZipEntry projectEntry = zipFile.getEntry(PROJECT_FILE);

      Multisample multisample = fromXML(new InputStreamReader(zipFile.getInputStream(projectEntry)), Multisample.class);

      zipFile.close();

      return multisample;
   }


   public static InputStream streamEmbedded(final File file, final String embeddedPath) throws IOException
   {
      ZipFile zipFile = new ZipFile(file);

      ZipEntry entry = zipFile.getEntry(embeddedPath);

      InputStream inputStream = zipFile.getInputStream(entry);

      return inputStream;
   }
}
