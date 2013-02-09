Java-Bytecode-Studio
====================

A set of utilities for Java bytecode.



##Obfuscator


####Example of code obfuscated by JBS and subsequently decompiled by the popular JD-GUI engine:

###Before

<pre lang="java"><code>
public class Test
{
  public static int integer = 1337;

  public static void main(String[] args) {
    count(10);
  }

  public static void count(int paramInt) {
    for (int i = 0; i != paramInt; i++) {
      System.out.println(i);
    }
    System.out.println("Good Job!");
  }
}
</code></pre>

###After


<pre lang="java"><code>
public class Test
{
  public static int d;

  // ERROR //
  public Test()
  {
    // Byte code:
    //   0: goto +16 -> 16
    //   3: pop
    //   4: aload_0
    //   5: invokespecial 1  java/lang/Object:<init>	()V
    //   8: aload_0
    //   9: ldc2_w 2
    //   12: putfield 4	Test:0	J
    //   15: return
    //   16: jsr -9 -> 7
    //   19: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	16	19	finally
  }

  public static void main(String[] paramArrayOfString)
  {
    count(10);
  }

  // ERROR //
  public static void count(int paramInt)
  {
    // Byte code:
    //   0: goto +33 -> 33
    //   3: pop
    //   4: iconst_0
    //   5: istore_1
    //   6: iload_1
    //   7: iload_0
    //   8: if_icmpeq +16 -> 24
    //   11: getstatic 17	java/lang/System:out	Ljava/io/PrintStream;
    //   14: iload_1
    //   15: invokevirtual 20	java/io/PrintStream:println	(I)V
    //   18: iinc 1 1
    //   21: goto -15 -> 6
    //   24: getstatic 17	java/lang/System:out	Ljava/io/PrintStream;
    //   27: ldc 21
    //   29: invokevirtual 19	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   32: return
    //   33: jsr -26 -> 7
    //   36: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	33	36	finally
  }

  // ERROR //
  static
  {
    // Byte code:
    //   0: goto +11 -> 11
    //   3: pop
    //   4: sipush 1337
    //   7: putstatic 9	Test:d	I
    //   10: return
    //   11: jsr -4 -> 7
    //   14: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	11	14	finally
  }
}
</code></pre>

##Disassembler
