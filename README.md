JBL - Java Bytecode Library
===========================

JBL, as its expanded name might suggest, is a library for manipulating JVM bytecode on the fly. It is written, as one might expect, in Java. Bytecode support for other languages is planned, but currently not supported. JBL attempts to merge the things which make other bytecode libraries, namely ASM, BCEL, and SERP useful, maintaining an equally simply (and sometimes simpler!) api, while not sacrificing usability by enforcing visitor pattern usage. JBL abstracts the internal bytecode in class files to trivial notions, yet also allows the delving into direct class pool manipulation et al. It also provides structures for doing common things (like class reference remapping), and has a simple OO structure making it easy for you to define your own. Additionally, each JBL object has methods you'd logically expect it to have, making it relatively simple to do work even without constantly glancing at the JBL JavaDocs.

###Defining a ClassFile object
For you to do anything, you must first define a ClassFile object. This is the core of JBL, and can be constructed simply, in many ways. You can pass a byte[], stream, or file. Or use its public no-args constructor. Whatever floats your boat.

<pre lang="java"><code>
ClassFile streamed = new ClassFile(inputStream); //An input stream, can be anything..
ClassFile flat = new ClassFile(new File("D:\myclass.class"); //A file...
ClassFile bytes = new ClassFile(new byte[0]); //! byte array...
ClassFile noArgs = new ClassFile(); //Or even empty, if you wish to start from scratch.
</code></pre>

Take note that JBL does minimal verification when loading files, so you may get funky behavior if you pass a truncated stream or such. JBL uses an incremental loading scheme to streamline class loading time, meaning classfile parts will only be loaded if they are requested. Overall, this increases ClassFile object construction speed to practically nothing, and allows light modification of the class (access flags etc.) to be done with less memory and latency.

###Visitor pattern
Though not enforced, if you are used with ASM or BCEL, you may find this more suitable to your taste. In some cases, ASM visitors will run out of the box when their extending classes are changed to point JBL classes. JBL is packaged with two visitor interfaces: ClassVisitor and MemberVisitor. The latter is a further abstraction over both method and field structures, since they share the same format. In most cases, however, simply manipulating a ClassFile object suffices and may be cleaner than using a visitor. Consider the following example:

<pre lang="java"><code>
public void publicWithVisitor(ClassFile clazz) {
   new ClassAdapter(new ClassVisitor() {
      @Override
    public int visitAccessFlags(int flag) {
	     return flag & Modifier.PUBLIC;
	  }
   }).adapt(clazz);
}

public void publicWOVisitor(ClassFile clazz) {
   clazz.setAccessFlags(clazz.getAccessFlags() & Modifier.PUBLIC);
}
</code></pre>

JBL also comes with an abstract Remapper class, which is simply a ClassVisitor implementation for remapping class references. Extending classes need only override `remap(String)`, yet are free to override more specific `remapSignature(String)` or `remapType(String)`, while still having the capabilities of their ClassVisitor superclass.

Please note that JBL visitors are still very much under heavy development, so the above information may come and go out of date as development continues.

###[WIP]Metadata
JBL puts a strong emphasis on metadata, its storage, and its ease-of-access. A new, and exciting feature is the introduction of the `Metadatable` interface. This interface further abstracts attribute pool modification, and, when used in conjunction with `XMLAttribute`, allows the storage of arbitrary information, like HashMaps, Collections, or even more complex structures like TreeMaps to be stored and later recovered from class files. Both the ClassFile and Member objects implement Metadatable, and though this is still a work in progress, many parts of the interface's implementation have been finished. With JBL, storing and accessing attribute metadata becomes as simple as:

<pre lang="java"><code>
ClassFile clazz = new ClassFile();
HashMap myMap = new HashMap();
clazz.setMetadata("my_map", myMap);
//Later on in your code, or after the dumping/reinstantiation of your class
HashMap myRecoveredMap = (HashMap)clazz.getMetadata("my_map");
</code></pre>

###Noteworthy notes
This is a bulleted collection of tips and tricks on the usage of JBL, which knowing will speed up developing with JBL.

* ClassFile and Member objects extends AccessibleMember, giving simple access to access flags via methods akin to `setSynchronized(boolean)` and `setPublic(boolean)`. All this means is you do not have to handle `getFlag()` masks directly.
* All JVM opcodes (currently with the exception of `INVOKEDYNAMIC`) reside in JBL's Opcode class. Also included are constant pool tag bytes, primitive array designations, and stack growth patterns.
* You can define your own Pool parser, via the `Pool.Parser` interface. Pool also contains preset parsers for classes, including `ATTRIBUTE_PARSER`, `CONSTANT_PARSER`, and `MEMBER_PARSER`, among others.
* A very rough work-in-progress code generator can be found in package `tk.jblib.generation`.
* Simple test programs that will help you get the hang of JBL can be found in root package `core`.

