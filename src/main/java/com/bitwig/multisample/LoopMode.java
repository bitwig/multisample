package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum LoopMode
{
   @XmlEnumValue(value = "off") off,
   @XmlEnumValue(value = "loop") loop,
   @XmlEnumValue(value = "ping-pong") pingPong
}
