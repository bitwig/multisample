# .multisample

A modern and open multi-sample file format for data exchange between sample-based instruments, to simplify the workflow for end users and sound designers. The format is limited to basic sample mappings, leaving out all playback and sound-shaping parameters to the preset of the relevant instrument.  

Authors: Bitwig & 
PreSonus Software

## Goals

* As a sample-player user I would like to transfer a single, playable instrument sound that contains multiple single samples across a music keyboard, with multiple velocity layers and possibly multiple samples playing simultaneously. [in scope]

* As a drum-sample-player user I would like to transfer a single drum or percussion instrument into an existing kit that contains multiple single samples in velocity layers, with multiple alternate samples (round-robin) per dynamic layer. [in scope]

## Non-goals

* As a producer, I would like to transfer a set of corresponding drum or instrument loops with multiple musical variations. [out of scope]

* As a sample-player user I would like to transfer a complex instrument containing multiple articulations and / or voicing (filters, envelopes, effects). [out of scope]

## Container Format

A multisample can be packed into a single file using the ZIP container format. The mapping information is stored in the multisample.xml file in the root folder of the archive.

Filename extension: .multisample

Alternatively, a file system folder can be used, following the same naming convention.

The ZIP file should embed files uncompressed using the STORED method to allow sample-data to be streamed without prior extraction.

## Audio Format

Embedded sample files should be RIFF WAVE files in either PCM or IEEE_FLOAT format. A player is expected to play back mono and stereo files with arbitrary sample-rates and bit-depths between 8 and 32.

Multichannel files are allowed, but it is not to be expected that all players support it.

## XML Structure

A multisample may contain groups, which organize samples into logical groups. A group does not have any meaning for the sound synthesis, it is used to organize samples in an editor.

A sample references an audio file on disk and adds mapping information. It can either be ungrouped (-1) or be part of a specified group.

## XML Schema

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">

   <xs:element name="group" type="group"/>

   <xs:element name="multisample" type="multisample"/>

   <xs:element name="sample" type="sample"/>

   <xs:complexType name="multisample">
      <xs:sequence>
         <xs:element name="generator" type="xs:string"/>
         <xs:element name="category" type="xs:string"/>
         <xs:element name="creator" type="xs:string"/>
         <xs:element name="description" type="xs:string" minOccurs="0"/>
         <xs:element name="keywords" minOccurs="0">
            <xs:complexType>
               <xs:sequence>
                  <xs:element name="keyword" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
               </xs:sequence>
            </xs:complexType>
         </xs:element>
         <xs:element ref="group" minOccurs="0" maxOccurs="unbounded"/>
         <xs:element ref="sample" minOccurs="0" maxOccurs="unbounded"/>
      </xs:sequence>
      <xs:attribute name="name" type="xs:string" use="required"/>
   </xs:complexType>

   <xs:complexType name="group">
      <xs:sequence/>
      <xs:attribute name="name" type="xs:string" use="required"/>
      <xs:attribute name="color" type="xs:string"/>
   </xs:complexType>

   <xs:complexType name="sample">
      <xs:sequence>
         <xs:element name="key" type="key"/>
         <xs:element name="velocity" type="range"/>
         <xs:element name="select" type="range"/>
         <xs:element name="loop" type="loop" minOccurs="0"/>
      </xs:sequence>
      <xs:attribute name="file" type="xs:string" use="required"/>
      <xs:attribute name="sample-start" type="xs:double"/>
      <xs:attribute name="sample-stop" type="xs:double"/>
      <xs:attribute name="gain" type="xs:double"/>
      <xs:attribute name="group" type="xs:int"/>
      <xs:attribute name="parameter-1" type="xs:double"/>
      <xs:attribute name="parameter-2" type="xs:double"/>
      <xs:attribute name="parameter-3" type="xs:double"/>
      <xs:attribute name="reverse" type="xs:boolean"/>
      <xs:attribute name="zone-logic" type="zoneLogic"/>
   </xs:complexType>

   <xs:complexType name="key">
      <xs:complexContent>
         <xs:extension base="range">
            <xs:sequence/>
            <xs:attribute name="root" type="xs:int"/>
            <xs:attribute name="track" type="xs:double"/>
            <xs:attribute name="tune" type="xs:double"/>
         </xs:extension>
      </xs:complexContent>
   </xs:complexType>

   <xs:complexType name="range">
      <xs:sequence/>
      <xs:attribute name="low" type="xs:int"/>
      <xs:attribute name="high" type="xs:int"/>
      <xs:attribute name="low-fade" type="xs:int"/>
      <xs:attribute name="high-fade" type="xs:int"/>
   </xs:complexType>

   <xs:complexType name="loop">
      <xs:sequence/>
      <xs:attribute name="mode" type="loopMode"/>
      <xs:attribute name="start" type="xs:double"/>
      <xs:attribute name="stop" type="xs:double"/>
      <xs:attribute name="fade" type="xs:double"/>
   </xs:complexType>

   <xs:simpleType name="zoneLogic">
      <xs:restriction base="xs:string">
         <xs:enumeration value="always-play"/>
         <xs:enumeration value="round-robin"/>
      </xs:restriction>
   </xs:simpleType>

   <xs:simpleType name="loopMode">
      <xs:restriction base="xs:string">
         <xs:enumeration value="off"/>
         <xs:enumeration value="loop"/>
         <xs:enumeration value="ping-pong"/>
      </xs:restriction>
   </xs:simpleType>
</xs:schema>
```