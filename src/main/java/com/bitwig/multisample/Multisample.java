package com.bitwig.multisample;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "multisample")
@XmlSeeAlso({Group.class, Sample.class})
public class Multisample
{
   /** 
    * Name of the multisample. 
    */
   @XmlAttribute(required = true)
   public String name;

   /** 
    * Software which generated the file.
    */
   @XmlElement(required = true)
   public String generator;

   /** Category of the multisample (ie Drum / Keyboard / FX) */
   @XmlElement(required = true)
   public String category;

   /** User who created the file. */
   @XmlElement(required = true)
   public String creator;

   /** A longer description of the multisample. */
   @XmlElement(required = false)
   public String description;

   @XmlElementWrapper(name = "keywords")
   @XmlElement(name = "keyword")
   public List<String> keywords = new ArrayList<>();

   @XmlElementRef
   public List<Group> groups = new ArrayList<>();

   @XmlElementRef
   public List<Sample> samples = new ArrayList<>();
}
