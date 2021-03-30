package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Key extends Range
{
   @XmlAttribute
   Integer root;

   /** Key-tracking range = [0 .. 2] default = 1 (100%) */
   @XmlAttribute
   Double track;

   /** Fine tuning or transposition of the sample, in (fractional) semitones. (default = 0.0) */
   @XmlAttribute
   Double tune;
}
