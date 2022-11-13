import os
fileInTest = os.listdir("test")
fileInSrc = os.listdir("src")
for i in range(1, len(fileInSrc)+1):
    os.system("gcc src/testfile{}.c".format(i))
    os.system("sed -n '3,$p' src/testfile{}.c > test/testfile{}.txt".format(i, i))
    os.system("./a.out < test/input{}.txt > test/output{}.txt".format(i, i))
os.system("cd test/ && zip -r ../test.zip . && cd ..")
