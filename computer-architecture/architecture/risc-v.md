## Hardware Platform Terminology

A RISC-V hardware platform can contain:

* one or more RISC-V compatible processing cores
* other non-RISC-V cores
* fixed-function accelators
* various physical memory structures, IO devices
* and interconnect structure to allow the components to communicate.

A **core** is an independent instruction fetch unit. 

// A RISC-V core might support multiple RISC-V compatible hardware threads, or *harts*, through multithreading. // TODO confusion

## RISC-V ISA Overview

A RISC-V ISA is defined as a **base integer ISA**, plus none or more extensions.

There are two primary base integer ISAs: **RV32I** and **RV64I**, which provide 32-bit and 64-bit address spaces respectively. Some other base integer ISAs:

* RV32E: subset variant of the RV32I instruction set, support small microcontrollers, which has half the number of integer registers.
* RV128I: in future, support 128-bit address space.

All base integer instruction sets use a two's complement representaiton for signed integer values.

Each base integer ISA can be extended with one or more optional instruction-set extensions.
An extension may be standard or non-standard. Standard extensions haven't conflict with the base or any other standard extensions. Current standard extensions are:

* integer multiplication and division extension, named **M**.
* atomic memory extension, denoted **A**, adds instructions that atomatically read, modify, and write memory for inter-processor synchronization.
* single-precision floating-point extension **F**, adds floating-point registers, single-precision loads, stores, and computational instructions.
* double-precision floating-point extension **D**, expands the floating-point regisers, and adds double-precision computational instructions, loads, and stores.
* standard **C** compressed instruction extension provides narrower 16-bit forms of common instructions.

Examples, RV32IFC, means base is RV32I, extensions are **F** and **C**

## Address space and memory

In RISC-V, address space is a single byte-addressable address space of 2^XLEN bytes, XLEN = length in bits of a integer register, with RV32I, XLEN = 32. A *word* of memory is 4 bytes, a *halfword* is 2 bytes, *doubleword* is 8 bytes, and *quadword* is 16 bytes.

The address space is circular, so that the byte add address 2^XLEN - 1 is adjacent to the byte at address zero. Address computations done by the hardware ignore overflow and instead wrap around modulo 2^XLEN.

The execution environment determines the mapping of hardware resources into an address space. Different address ranges may be: // TODO need more information

* vacant (empty, nothing)
* main memory
* I/O devices

Reads and writes of I/O devices may have visible side effects, but accesses to main memory cannot.

## Instruction Length Encoding

The base ISA has fixed-length 32-bit instructions. However, the standard RISC-V encoding scheme is designed to support ISA extensions with variable-length instructions: 16, 32, 48, 64, ....

```js
                                       | xxxxxxxxxxxxxxaa |  16-bit (aa != 11)
                    | xxxxxxxxxxxxxxxx | xxxxxxxxxxxbbb11 |  32-bit (bbb != 111)
            ...xxxx | xxxxxxxxxxxxxxxx | xxxxxxxxxx011111 |  48-bit
            ...xxxx | xxxxxxxxxxxxxxxx | xxxxxxxxx0111111 |  64-bit
            ...xxxx | xxxxxxxxxxxxxxxx | xnnnxxxxx1111111 |  (80+16*nnn)-bit, nnn != 111
            ...xxxx | xxxxxxxxxxxxxxxx | x111xxxxx1111111 |  Reserved for >= 192-bits
Byte address  base+4             base+2                base
```

Only the 16-bit and 32-bit encoding are considered frozen at this time.

The compressed **C** extension ISA reduces the code size and enery saving by using both 16-bit and 32-bit length instructions.


## RV32I Base Integer Instruction Set

Instruction abbreviations format

>        Set Less Than { _ | Immediate } { _ | Unsigned }

The above format represents four instructions: `slt, slti, sltu, sltiu`.

Instruction diagram shows all RV32I instructions

*Integer computation*

>       ADD { _ | Immediate }
>       SUBtract
>       { AND | OR | eXclusice OR } { _ | Immediate }
>       { Shift Left Logical | Shift Right Arithmetic | Shift Right Logical } { _ | Immediate }

*Control transfer*

>       Branch { Equal | Not Equal }
>       Branch { Greater than or Equal | Less Than }
>       Jump And Link { _ | Register }

*Loads and stores*

>       { Load | Store } { Byte | Halfword | Word }
>       Load { Byte | Halfword } Unsigned

*Miscellaneous instructions*

>       FENCE loads & stores
>       FENCE.Instruction & data
>       Environment { BREAK | CALL }
>       Control Status Register { Read & Clear bit | Read & Set bit | Read & Write }

### Instruction Format

All instructions are a fixed 32 bits length, and must be aligned on a four-byte boundary in memory.
An instruction-address-misaligned exception is reported on the branch or jump instruction, not on the target instruction. (???)

There are six instruction formats:

```js
 31        25 24      20 19      15 14      12 11      7 6        0
|  funct7    |    rs2   |    rs1   |  funct3  |    rd   |  opcode  | R-type
|         i[11:0]       |    rs1   |  funct3  |    rd   |  opcode  | I-type
|   i[11:5]  |    rs2   |    rs1   |  funct3  |  i[4:0] |  opcode  | S-type
|i[12][10:5] |    rs2   |    rs1   |  funct3  |i[4:1][11]  opcode  | B-type
|                   i[31:12]                  |    rd   |  opcode  | U-type
|i[20]|  i[10:1]  |i[11]|      i[19:12]       |    rd   |  opcode  | J-type
```

* R-type for register-register operations
* I-type for short immediates and loads
* S-type for stores
* B-type for conditional branches (same as S-type but the immediate field is rotated 1 bit)
* U-type for long immediates
* J-type for unconditional jumps (same as U-type but the immediate field is rotated 12 bits)

* *i* is immediate field, `i[x]` is the bit position in the immediate value being produced, not the bit position in the instruction. (???)
* immediate fields are always sign extended, and the sign bit is always in the most significant (in the left) of the instruction.
* to help programmers, a bit pattern of all zeros is an illegal RV32I instruction. Thus, erroneous jumps into zeroed memory regions will immediately trap, an aid to debugging. Similary, the bit pattern of all ones is an illegal instruction, which will trap other common errors such as unprogrammed non-volatile memory devices, disconnected memory buses, or broken memory chips.

### Registers

32 registers, each 32 bits width, and a register called pc - program counter

* register x0 is hardwired with all bits 0.
* general purpose registers x1-x31
* *pc* program counter (32 bits width too) holds the address of the current instruction