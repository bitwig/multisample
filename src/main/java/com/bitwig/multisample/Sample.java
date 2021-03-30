package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sample
{
   /** Relative path to audio file. */
   @XmlAttribute(required = true)
   public String file;

   /** Sample start position in samples (default = 0.0).
    *  Fractional positions are allowed
    *  */
   @XmlAttribute(name = "sample-start")
   public Double sampleStart;

   /** Sample end position (default = file length in samples).
    * Fractional positions are allowed
    * */
   @XmlAttribute(name = "sample-stop")
   public Double sampleStop;

   /** Gain correction in decibels (default = 0.0) */
   @XmlAttribute
   public Double gain;

   /** Index of group (default = -1 = ungrouped) */
   @XmlAttribute
   public Integer group;

   /** Exposed as P1 modulation source */
   @XmlAttribute(name = "parameter-1")
   public Double parameter1;

   /** Exposed as P2 modulation source */
   @XmlAttribute(name = "parameter-2")
   public Double parameter2;

   /** Exposed as P3 modulation source */
   @XmlAttribute(name = "parameter-3")
   public Double parameter3;

   /** Reverse playback when true */
   @XmlAttribute
   public Boolean reverse;

   /** Zone logic, always-play or round-robin */
   @XmlAttribute(name = "zone-logic")
   public ZoneLogic zoneLogic = ZoneLogic.alwaysPlay;

   @XmlElement(required = true)
   public Key key = new Key();

   @XmlElement(required = true)
   public Range velocity = new Range();

   @XmlElement(required = true)
   public Range select = new Range();

   @XmlElement(required = false)
   public Loop loop = new Loop();
}
