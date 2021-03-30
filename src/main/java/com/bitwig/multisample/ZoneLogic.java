package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum ZoneLogic
{
   @XmlEnumValue("always-play") alwaysPlay,
   @XmlEnumValue("round-robin") roundRobin,
}
