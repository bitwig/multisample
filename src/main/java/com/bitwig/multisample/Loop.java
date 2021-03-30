package com.bitwig.multisample;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Loop
{
   /** Loop mode - off/loop/ping-pong */
   @XmlAttribute
   public LoopMode mode;

   /** Loop start in samples.
    * Fractional positions are allowed and can be used for loop tuning.
    * */
   @XmlAttribute
   public Double start;

   /** Loop stop (end) in samples.
    * Fractional positions are allowed and can be used for loop tuning.
    * */
   @XmlAttribute
   public Double stop;

   /** Fade length [0 .. 1]
    Multiply with (stop - start) to the get crossfade length in samples
    */
    @XmlAttribute
   public Double fade;
}
