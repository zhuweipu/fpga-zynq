## Step 0: Install the RISC-V tools

Set the environment variable, `RISCV`, for the path where the RISC-V will be installed. Run `./build-riscv-tools.sh`.

## Step 1: Put your files in `buildroot-overlay/root`

Also, change the content in `buildroot-overlay/etc/init.d/rcS`.

## Step 2: Build Linux

Run `make`.

## Step 3: Test with `spike`.

Run `spike bblvmlinux`.

## Step 4: Run in the Zynq board.

Copy `bblvmlinux` to the SD card, and run `./fesvr-zynq bblvmlinux`.

## TODO

Use FireMarshal: https://github.com/firesim/firesim-software
