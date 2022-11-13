.data
str_2: .asciiz ", "
str_8: .asciiz "SBC step 1: expect:"
str_4: .asciiz "19373384\n"
str_7: .asciiz "\n"
str_6: .asciiz ", output: "
str_9: .asciiz "output:"
str_1: .asciiz "["
str_3: .asciiz "]\n"
str_5: .asciiz "Inner product: expect "
var_global_counter_1: .word  0 


.text
jal _main
ori $v0, $0, 10
syscall

_printVector: 
sub $sp, $sp, 88
sw $31, 76($sp)
label8_funcBlock:

#GetParam{to=sp0, num=0}
sw $a0, 0($sp)


#GetParam{to=sp4, num=1}
sw $a1, 4($sp)


#Move{to=sp24, from=1}
li $k0, 1
sw $k0, 24($sp)


#Move{to=sp20, from=0}
li $k0, 0
sw $k0, 20($sp)


#Mul{to=sp16, from1=sp24, from2=0}
lw $k0, 24($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 16($sp)


#Add{to=sp20, from1=sp20, from2=sp16}
lw $k0, 20($sp)
lw $k1, 16($sp)
addu $k0, $k0, $k1
sw $k0, 20($sp)


#Mul{to=sp24, from1=sp24, from2=0}
lw $k0, 24($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 24($sp)


#Lw{to=sp28, arr=sp0, i=sp20}
lw $k0, 0($sp)
lw $k1, 20($sp)
sll $k1, $k1, 2
addu $k1, $k0, $k1
lw $k0, 0($k1)
sw $k0, 28($sp)


#PrintStr{toPrint=str_1}
la $a0, str_1
li $v0, 4
syscall


#PrintInt{toPrint=sp28}
lw $a0, 28($sp)
li $v0, 1
syscall


#Move{to=sp68, from=1}
li $k0, 1
sw $k0, 68($sp)


#Lt{to=sp36, from1=sp68, from2=sp4}
lw $k0, 68($sp)
lw $k1, 4($sp)
slt $k1, $k0, $k1
sw $k1, 36($sp)


#JmpFalse{target=label9_whileEnd, from=sp36}
lw $k0, 36($sp)
beq $0, $k0, label9_whileEnd

label10_whileLoopStmt:

#Move{to=sp56, from=1}
li $k0, 1
sw $k0, 56($sp)


#Move{to=sp52, from=0}
li $k0, 0
sw $k0, 52($sp)


#Mul{to=sp48, from1=sp56, from2=sp68}
lw $k0, 56($sp)
lw $k1, 68($sp)
mul $k0, $k0, $k1
sw $k0, 48($sp)


#Add{to=sp52, from1=sp52, from2=sp48}
lw $k0, 52($sp)
lw $k1, 48($sp)
addu $k0, $k0, $k1
sw $k0, 52($sp)


#Mul{to=sp56, from1=sp56, from2=0}
lw $k0, 56($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 56($sp)


#Lw{to=sp60, arr=sp0, i=sp52}
lw $k0, 0($sp)
lw $k1, 52($sp)
sll $k1, $k1, 2
addu $k1, $k0, $k1
lw $k0, 0($k1)
sw $k0, 60($sp)


#PrintStr{toPrint=str_2}
la $a0, str_2
li $v0, 4
syscall


#PrintInt{toPrint=sp60}
lw $a0, 60($sp)
li $v0, 1
syscall


#Add{to=sp64, from1=sp68, from2=1}
lw $k0, 68($sp)
addu $k0, $k0, 1
sw $k0, 64($sp)


#Move{to=sp68, from=sp64}
lw $k0, 64($sp)
sw $k0, 68($sp)

label11_rejudgeAfterHitContinue:

#Lt{to=sp72, from1=sp68, from2=sp4}
lw $k0, 68($sp)
lw $k1, 4($sp)
slt $k1, $k0, $k1
sw $k1, 72($sp)


#JmpTrue{target=label10_whileLoopStmt, from=sp72}
lw $k0, 72($sp)
bne $0, $k0, label10_whileLoopStmt

label9_whileEnd:

#PrintStr{toPrint=str_3}
la $a0, str_3
li $v0, 4
syscall


#Return{retVal=void}
lw $31, 76($sp)
add $sp, $sp, 88
jr $ra


_innerProduct: 
sub $sp, $sp, 112
sw $31, 96($sp)
label0_funcBlock:

#GetParam{to=sp0, num=0}
sw $a0, 0($sp)


#GetParam{to=sp4, num=1}
sw $a1, 4($sp)


#GetParam{to=sp8, num=2}
sw $a2, 8($sp)


#Move{to=sp88, from=0}
li $k0, 0
sw $k0, 88($sp)


#Move{to=sp80, from=0}
li $k0, 0
sw $k0, 80($sp)


#Lt{to=sp20, from1=sp88, from2=sp8}
lw $k0, 88($sp)
lw $k1, 8($sp)
slt $k1, $k0, $k1
sw $k1, 20($sp)


#JmpFalse{target=label1_whileEnd, from=sp20}
lw $k0, 20($sp)
beq $0, $k0, label1_whileEnd

label2_whileLoopStmt:

#Move{to=sp40, from=1}
li $k0, 1
sw $k0, 40($sp)


#Move{to=sp36, from=0}
li $k0, 0
sw $k0, 36($sp)


#Mul{to=sp32, from1=sp40, from2=sp88}
lw $k0, 40($sp)
lw $k1, 88($sp)
mul $k0, $k0, $k1
sw $k0, 32($sp)


#Add{to=sp36, from1=sp36, from2=sp32}
lw $k0, 36($sp)
lw $k1, 32($sp)
addu $k0, $k0, $k1
sw $k0, 36($sp)


#Mul{to=sp40, from1=sp40, from2=0}
lw $k0, 40($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 40($sp)


#Lw{to=sp44, arr=sp0, i=sp36}
lw $k0, 0($sp)
lw $k1, 36($sp)
sll $k1, $k1, 2
addu $k1, $k0, $k1
lw $k0, 0($k1)
sw $k0, 44($sp)


#Move{to=sp64, from=1}
li $k0, 1
sw $k0, 64($sp)


#Move{to=sp60, from=0}
li $k0, 0
sw $k0, 60($sp)


#Mul{to=sp56, from1=sp64, from2=sp88}
lw $k0, 64($sp)
lw $k1, 88($sp)
mul $k0, $k0, $k1
sw $k0, 56($sp)


#Add{to=sp60, from1=sp60, from2=sp56}
lw $k0, 60($sp)
lw $k1, 56($sp)
addu $k0, $k0, $k1
sw $k0, 60($sp)


#Mul{to=sp64, from1=sp64, from2=0}
lw $k0, 64($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 64($sp)


#Lw{to=sp68, arr=sp4, i=sp60}
lw $k0, 4($sp)
lw $k1, 60($sp)
sll $k1, $k1, 2
addu $k1, $k0, $k1
lw $k0, 0($k1)
sw $k0, 68($sp)


#Mul{to=sp72, from1=sp44, from2=sp68}
lw $k0, 44($sp)
lw $k1, 68($sp)
mul $k0, $k0, $k1
sw $k0, 72($sp)


#Add{to=sp76, from1=sp80, from2=sp72}
lw $k0, 80($sp)
lw $k1, 72($sp)
addu $k0, $k0, $k1
sw $k0, 76($sp)


#Move{to=sp80, from=sp76}
lw $k0, 76($sp)
sw $k0, 80($sp)


#Add{to=sp84, from1=sp88, from2=1}
lw $k0, 88($sp)
addu $k0, $k0, 1
sw $k0, 84($sp)


#Move{to=sp88, from=sp84}
lw $k0, 84($sp)
sw $k0, 88($sp)

label3_rejudgeAfterHitContinue:

#Lt{to=sp92, from1=sp88, from2=sp8}
lw $k0, 88($sp)
lw $k1, 8($sp)
slt $k1, $k0, $k1
sw $k1, 92($sp)


#JmpTrue{target=label2_whileLoopStmt, from=sp92}
lw $k0, 92($sp)
bne $0, $k0, label2_whileLoopStmt

label1_whileEnd:

#Return{retVal=sp80}
lw $v0, 80($sp)
lw $31, 96($sp)
add $sp, $sp, 112
jr $ra


#Return{retVal=void}
lw $31, 96($sp)
add $sp, $sp, 112
jr $ra


_main: 
sub $sp, $sp, 244
sw $31, 240($sp)
label12_MainFuncBlock:

#PrintStr{toPrint=str_4}
la $a0, str_4
li $v0, 4
syscall


#Malloc{arr=array0, len=10}


#Sw{from=1, arr=array0, i=0}
addi $k0, $sp, 0
li $k1, 1
sw $k1, 0($k0)


#Sw{from=3, arr=array0, i=1}
addi $k0, $sp, 0
li $k1, 3
sw $k1, 4($k0)


#Sw{from=5, arr=array0, i=2}
addi $k0, $sp, 0
li $k1, 5
sw $k1, 8($k0)


#Sw{from=7, arr=array0, i=3}
addi $k0, $sp, 0
li $k1, 7
sw $k1, 12($k0)


#Sw{from=9, arr=array0, i=4}
addi $k0, $sp, 0
li $k1, 9
sw $k1, 16($k0)


#Sw{from=11, arr=array0, i=5}
addi $k0, $sp, 0
li $k1, 11
sw $k1, 20($k0)


#Sw{from=2, arr=array0, i=6}
addi $k0, $sp, 0
li $k1, 2
sw $k1, 24($k0)


#Sw{from=9, arr=array0, i=7}
addi $k0, $sp, 0
li $k1, 9
sw $k1, 28($k0)


#Sw{from=7, arr=array0, i=8}
addi $k0, $sp, 0
li $k1, 7
sw $k1, 32($k0)


#Sw{from=8, arr=array0, i=9}
addi $k0, $sp, 0
li $k1, 8
sw $k1, 36($k0)


#Malloc{arr=array40, len=10}


#Sw{from=2, arr=array40, i=0}
addi $k0, $sp, 40
li $k1, 2
sw $k1, 0($k0)


#Sw{from=5, arr=array40, i=1}
addi $k0, $sp, 40
li $k1, 5
sw $k1, 4($k0)


#Sw{from=4, arr=array40, i=2}
addi $k0, $sp, 40
li $k1, 4
sw $k1, 8($k0)


#Sw{from=8, arr=array40, i=3}
addi $k0, $sp, 40
li $k1, 8
sw $k1, 12($k0)


#Sw{from=9, arr=array40, i=4}
addi $k0, $sp, 40
li $k1, 9
sw $k1, 16($k0)


#Sw{from=6, arr=array40, i=5}
addi $k0, $sp, 40
li $k1, 6
sw $k1, 20($k0)


#Sw{from=3, arr=array40, i=6}
addi $k0, $sp, 40
li $k1, 3
sw $k1, 24($k0)


#Sw{from=7, arr=array40, i=7}
addi $k0, $sp, 40
li $k1, 7
sw $k1, 28($k0)


#Sw{from=1, arr=array40, i=8}
addi $k0, $sp, 40
li $k1, 1
sw $k1, 32($k0)


#Sw{from=1, arr=array40, i=9}
addi $k0, $sp, 40
li $k1, 1
sw $k1, 36($k0)


#Malloc{arr=array80, len=10}


#Malloc{arr=array120, len=10}


#Move{to=sp176, from=0}
li $k0, 0
sw $k0, 176($sp)


#SetParam{paramItem=array0, num=0}
addi $a0, $sp, 0


#SetParam{paramItem=10, num=1}
li $a1, 10


#Call{funcLabel=_printVector}
jal _printVector


#GetRet{to=sp164}
sw $v0, 164($sp)


#SetParam{paramItem=array40, num=0}
addi $a0, $sp, 40


#SetParam{paramItem=10, num=1}
li $a1, 10


#Call{funcLabel=_printVector}
jal _printVector


#GetRet{to=sp168}
sw $v0, 168($sp)


#SetParam{paramItem=array0, num=0}
addi $a0, $sp, 0


#SetParam{paramItem=array40, num=1}
addi $a1, $sp, 40


#SetParam{paramItem=10, num=2}
li $a2, 10


#Call{funcLabel=_innerProduct}
jal _innerProduct


#GetRet{to=sp172}
sw $v0, 172($sp)


#Move{to=sp176, from=sp172}
lw $k0, 172($sp)
sw $k0, 176($sp)


#Move{to=sp180, from=324}
li $k0, 324
sw $k0, 180($sp)


#PrintStr{toPrint=str_5}
la $a0, str_5
li $v0, 4
syscall


#PrintInt{toPrint=sp180}
lw $a0, 180($sp)
li $v0, 1
syscall


#PrintStr{toPrint=str_6}
la $a0, str_6
li $v0, 4
syscall


#PrintInt{toPrint=sp176}
lw $a0, 176($sp)
li $v0, 1
syscall


#PrintStr{toPrint=str_7}
la $a0, str_7
li $v0, 4
syscall


#Neq{to=sp184, from1=sp180, from2=sp176}
lw $k0, 180($sp)
lw $k1, 176($sp)
sne $k1, $k0, $k1
sw $k1, 184($sp)


#JmpFalse{target=label14_ifEnd, from=sp184}
lw $k0, 184($sp)
beq $0, $k0, label14_ifEnd

label13_ifStmt:

#Return{retVal=1}
li $v0, 1
lw $31, 240($sp)
add $sp, $sp, 244
jr $ra

label14_ifEnd:

#SetParam{paramItem=array80, num=0}
addi $a0, $sp, 80


#SetParam{paramItem=array0, num=1}
addi $a1, $sp, 0


#SetParam{paramItem=2, num=2}
li $a2, 2


#SetParam{paramItem=array40, num=3}
addi $a3, $sp, 40


#SetParam{paramItem=3, num=4}
li $k0, 3
sw $k0, -16($sp)


#SetParam{paramItem=4, num=5}
li $k0, 4
sw $k0, -20($sp)


#SetParam{paramItem=10, num=6}
li $k0, 10
sw $k0, -24($sp)


#Call{funcLabel=_scaleBiasCombination}
jal _scaleBiasCombination


#GetRet{to=sp188}
sw $v0, 188($sp)


#Malloc{arr=array192, len=10}


#Sw{from=12, arr=array192, i=0}
addi $k0, $sp, 192
li $k1, 12
sw $k1, 0($k0)


#Sw{from=25, arr=array192, i=1}
addi $k0, $sp, 192
li $k1, 25
sw $k1, 4($k0)


#Sw{from=26, arr=array192, i=2}
addi $k0, $sp, 192
li $k1, 26
sw $k1, 8($k0)


#Sw{from=42, arr=array192, i=3}
addi $k0, $sp, 192
li $k1, 42
sw $k1, 12($k0)


#Sw{from=49, arr=array192, i=4}
addi $k0, $sp, 192
li $k1, 49
sw $k1, 16($k0)


#Sw{from=44, arr=array192, i=5}
addi $k0, $sp, 192
li $k1, 44
sw $k1, 20($k0)


#Sw{from=17, arr=array192, i=6}
addi $k0, $sp, 192
li $k1, 17
sw $k1, 24($k0)


#Sw{from=43, arr=array192, i=7}
addi $k0, $sp, 192
li $k1, 43
sw $k1, 28($k0)


#Sw{from=21, arr=array192, i=8}
addi $k0, $sp, 192
li $k1, 21
sw $k1, 32($k0)


#Sw{from=23, arr=array192, i=9}
addi $k0, $sp, 192
li $k1, 23
sw $k1, 36($k0)


#PrintStr{toPrint=str_8}
la $a0, str_8
li $v0, 4
syscall


#SetParam{paramItem=array192, num=0}
addi $a0, $sp, 192


#SetParam{paramItem=10, num=1}
li $a1, 10


#Call{funcLabel=_printVector}
jal _printVector


#GetRet{to=sp232}
sw $v0, 232($sp)


#PrintStr{toPrint=str_9}
la $a0, str_9
li $v0, 4
syscall


#SetParam{paramItem=array80, num=0}
addi $a0, $sp, 80


#SetParam{paramItem=10, num=1}
li $a1, 10


#Call{funcLabel=_printVector}
jal _printVector


#GetRet{to=sp236}
sw $v0, 236($sp)


#Return{retVal=0}
li $v0, 0
lw $31, 240($sp)
add $sp, $sp, 244
jr $ra


#Return{retVal=void}
lw $31, 240($sp)
add $sp, $sp, 244
jr $ra


_scaleBiasCombination: 
sub $sp, $sp, 180
sw $31, 148($sp)
label4_funcBlock:

#GetParam{to=sp0, num=0}
sw $a0, 0($sp)


#GetParam{to=sp4, num=1}
sw $a1, 4($sp)


#GetParam{to=sp8, num=2}
sw $a2, 8($sp)


#GetParam{to=sp12, num=3}
sw $a3, 12($sp)


#GetParam{to=sp16, num=4}
lw $k0, 164($sp)
sw $k0, 16($sp)


#GetParam{to=sp20, num=5}
lw $k0, 160($sp)
sw $k0, 20($sp)


#GetParam{to=sp24, num=6}
lw $k0, 156($sp)
sw $k0, 24($sp)


#Move{to=sp132, from=0}
li $k0, 0
sw $k0, 132($sp)


#Lt{to=sp32, from1=sp132, from2=sp24}
lw $k0, 132($sp)
lw $k1, 24($sp)
slt $k1, $k0, $k1
sw $k1, 32($sp)


#JmpFalse{target=label5_whileEnd, from=sp32}
lw $k0, 32($sp)
beq $0, $k0, label5_whileEnd

label6_whileLoopStmt:

#Move{to=sp52, from=1}
li $k0, 1
sw $k0, 52($sp)


#Move{to=sp48, from=0}
li $k0, 0
sw $k0, 48($sp)


#Mul{to=sp44, from1=sp52, from2=sp132}
lw $k0, 52($sp)
lw $k1, 132($sp)
mul $k0, $k0, $k1
sw $k0, 44($sp)


#Add{to=sp48, from1=sp48, from2=sp44}
lw $k0, 48($sp)
lw $k1, 44($sp)
addu $k0, $k0, $k1
sw $k0, 48($sp)


#Mul{to=sp52, from1=sp52, from2=0}
lw $k0, 52($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 52($sp)


#Move{to=sp72, from=1}
li $k0, 1
sw $k0, 72($sp)


#Move{to=sp68, from=0}
li $k0, 0
sw $k0, 68($sp)


#Mul{to=sp64, from1=sp72, from2=sp132}
lw $k0, 72($sp)
lw $k1, 132($sp)
mul $k0, $k0, $k1
sw $k0, 64($sp)


#Add{to=sp68, from1=sp68, from2=sp64}
lw $k0, 68($sp)
lw $k1, 64($sp)
addu $k0, $k0, $k1
sw $k0, 68($sp)


#Mul{to=sp72, from1=sp72, from2=0}
lw $k0, 72($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 72($sp)


#Lw{to=sp76, arr=sp4, i=sp68}
lw $k0, 4($sp)
lw $k1, 68($sp)
sll $k1, $k1, 2
addu $k1, $k0, $k1
lw $k0, 0($k1)
sw $k0, 76($sp)


#Mul{to=sp80, from1=sp76, from2=sp8}
lw $k0, 76($sp)
lw $k1, 8($sp)
mul $k0, $k0, $k1
sw $k0, 80($sp)


#Move{to=sp100, from=1}
li $k0, 1
sw $k0, 100($sp)


#Move{to=sp96, from=0}
li $k0, 0
sw $k0, 96($sp)


#Mul{to=sp92, from1=sp100, from2=sp132}
lw $k0, 100($sp)
lw $k1, 132($sp)
mul $k0, $k0, $k1
sw $k0, 92($sp)


#Add{to=sp96, from1=sp96, from2=sp92}
lw $k0, 96($sp)
lw $k1, 92($sp)
addu $k0, $k0, $k1
sw $k0, 96($sp)


#Mul{to=sp100, from1=sp100, from2=0}
lw $k0, 100($sp)
li $k1, 0
mul $k0, $k0, $k1
sw $k0, 100($sp)


#Lw{to=sp104, arr=sp12, i=sp96}
lw $k0, 12($sp)
lw $k1, 96($sp)
sll $k1, $k1, 2
addu $k1, $k0, $k1
lw $k0, 0($k1)
sw $k0, 104($sp)


#Mul{to=sp108, from1=sp104, from2=sp16}
lw $k0, 104($sp)
lw $k1, 16($sp)
mul $k0, $k0, $k1
sw $k0, 108($sp)


#Add{to=sp112, from1=sp80, from2=sp108}
lw $k0, 80($sp)
lw $k1, 108($sp)
addu $k0, $k0, $k1
sw $k0, 112($sp)


#Add{to=sp116, from1=sp112, from2=sp20}
lw $k0, 112($sp)
lw $k1, 20($sp)
addu $k0, $k0, $k1
sw $k0, 116($sp)


#Lw{to=sp120, arr=var_global_counter_1, i=0}
la $k0, var_global_counter_1
lw $k0, 0($k0)
sw $k0, 120($sp)


#Add{to=sp124, from1=sp116, from2=sp120}
lw $k0, 116($sp)
lw $k1, 120($sp)
addu $k0, $k0, $k1
sw $k0, 124($sp)


#Sw{from=sp124, arr=sp0, i=sp48}
lw $k0, 0($sp)
lw $k1, 48($sp)
sll $k1, $k1, 2
addu $k0, $k0, $k1
lw $k1, 124($sp)
sw $k1, 0($k0)


#Add{to=sp128, from1=sp132, from2=1}
lw $k0, 132($sp)
addu $k0, $k0, 1
sw $k0, 128($sp)


#Move{to=sp132, from=sp128}
lw $k0, 128($sp)
sw $k0, 132($sp)

label7_rejudgeAfterHitContinue:

#Lt{to=sp136, from1=sp132, from2=sp24}
lw $k0, 132($sp)
lw $k1, 24($sp)
slt $k1, $k0, $k1
sw $k1, 136($sp)


#JmpTrue{target=label6_whileLoopStmt, from=sp136}
lw $k0, 136($sp)
bne $0, $k0, label6_whileLoopStmt

label5_whileEnd:

#Lw{to=sp140, arr=var_global_counter_1, i=0}
la $k0, var_global_counter_1
lw $k0, 0($k0)
sw $k0, 140($sp)


#Add{to=sp144, from1=sp140, from2=1}
lw $k0, 140($sp)
addu $k0, $k0, 1
sw $k0, 144($sp)


#Sw{from=sp144, arr=var_global_counter_1, i=0}
la $k0, var_global_counter_1
lw $k1, 144($sp)
sw $k1, 0($k0)


#Return{retVal=void}
lw $31, 148($sp)
add $sp, $sp, 180
jr $ra


