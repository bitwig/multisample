package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Group
{
   @XmlAttribute(required = true)
   public String name;

   @XmlAttribute
   public String color;
}
