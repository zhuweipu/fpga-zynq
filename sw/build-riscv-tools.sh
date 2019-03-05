git clone -n https://github.com/firesim/riscv-tools.git
cd riscv-tools
git checkout 1859daea5e71809af62a6d4eb12d8f203c7efbc8
git submodule update --init

cd riscv-gnu-toolchain
git submodule init
git submodule update riscv-binutils-gdb 
git submodule update riscv-gcc
git submodule update riscv-glibc
git submodule update riscv-newlib
git submodule update riscv-dejagnu

./build.sh
cd riscv-gnu-toolchain/build
make linux

cd ../..
