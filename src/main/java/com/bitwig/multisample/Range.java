package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Range
{
   @XmlAttribute Integer low;
   @XmlAttribute Integer high;
   @XmlAttribute(name = "low-fade") Integer lowFade;
   @XmlAttribute(name = "high-fade") Integer highFade;
}
