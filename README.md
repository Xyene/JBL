Java-Bytecode-Studio
====================

A set of utilities for Java bytecode.

Utilities (as of this writing):

* Obfuscator


Example of obfuscated code decompiled by the popular JD-GUI engine.

Before

<pre lang="java"><code>
public class Test
{
  public static int integer = 1337;

  public static void main(String[] paramArrayOfString) {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Testing: ");
    localStringBuilder.append(integer);
    count(10);
    try
    {
      throwException(new Exception(localStringBuilder.toString()));
    } catch (Exception localException) {
      localException.printStackTrace();
      System.out.println("Exception caught!");
    }
  }

  public static void count(int paramInt) {
    for (int i = 0; i != paramInt; i++) {
      System.out.println(i);
    }
    System.out.println("Good Job!");
  }

  public static void throwException(Exception paramException) throws Exception {
    throw paramException;
  }

  public void throwDynamic(Exception paramException) throws Exception {
    throwException(paramException);
  }
}
</code></pre>

After


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
    //   12: putfield 4	PhantomTest:0	J
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
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Testing: ");
    localStringBuilder.append(d);
    count(10);
    try
    {
      throwException(new Exception(localStringBuilder.toString()));
    } catch (Exception localException) {
      localException.printStackTrace();
      System.out.println("Exception caught!");
    }
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
  public static void throwException(Exception paramException)
  {
    // Byte code:
    //   0: goto +6 -> 6
    //   3: pop
    //   4: aload_0
    //   5: athrow
    //   6: jsr +1 -> 7
    //   9: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	6	9	finally
  }

  // ERROR //
  public void throwDynamic(Exception paramException)
  {
    // Byte code:
    //   0: goto +9 -> 9
    //   3: pop
    //   4: aload_1
    //   5: invokestatic 15	PhantomTest:throwException	(Ljava/lang/Exception;)V
    //   8: return
    //   9: jsr -2 -> 7
    //   12: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	9	12	finally
  }

  // ERROR //
  static
  {
    // Byte code:
    //   0: goto +11 -> 11
    //   3: pop
    //   4: sipush 1337
    //   7: putstatic 9	PhantomTest:d	I
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

* Disassembler
