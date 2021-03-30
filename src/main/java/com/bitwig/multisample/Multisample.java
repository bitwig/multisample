package com.bitwig.multisample;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "multisample")
@XmlSeeAlso({Group.class, Sample.class})
public class Multisample
{
   @XmlAttribute(required = true)
   public String name;

   @XmlElement(required = true)
   public String generator;

   @XmlElement(required = true)
   public String category;

   @XmlElement(required = true)
   public String creator;

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
