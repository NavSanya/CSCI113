
import java.util.*;

public class BoothSimulator {

    boolean carryFlag = false;
    static boolean isTwoCompMDCreated = false, save2sComp = true;
    // Registers used in booth simulation process.
    static String ac[] = new String[16], md[] = new String[16], mq[] = new String[16], md1[] = new String[16],
            md2sComp[] = new String[16], mq1 = "0";

    /**
     * main This method is the main driver of booth simulation. Takes input for
     * multiplicand and multiplier from user. Passes the input to booth
     * multiplier which in return gives the product that is finally displayed.
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the multiplicand: ");
        String multiplicand = scan.nextLine();
        System.out.print("Enter the multiplier: ");
        String multiplier = scan.next();
        scan.close();

        BoothSimulator boothSimulator = new BoothSimulator();
        String product = boothSimulator.boothMultiplier(multiplicand, multiplier);
        System.out.println("Product:" + product);
    }

    /**
     * boothMultiplier This method takes in multiplicand and multiplier from
     * main driver. Initializes the ACC, MD, MQ, MQ-1 registers and cycle
     * counter. Calls the 16bit Alu while cycle counter equals zero.
     *
     * @param multiplicand
     * @param multiplier
     * @return product 32bit product of multiplicand and multiplier.
     */
    public String boothMultiplier(String multiplicand, String multiplier) {
        String product = "";
        int counter = 16;
        for (int i = 0; i < counter; i++) {
            ac[i] = "0";
            md[i] = String.valueOf(multiplicand.charAt(i));
            md1[i] = String.valueOf(multiplicand.charAt(i));
            mq[i] = String.valueOf(multiplier.charAt(i));
        }
        if (mq[0].equals("1")) {
            save2sComp = false;
            mq = twosCompliment(mq);
            save2sComp = true;
        }
        System.out.println("Counter			MD			AC			MQ		MQ-1		");
        while (counter != 0) {
            boolean addSubFlag = false;
            if ((mq[mq.length - 1].equals("0") && mq1.equals("1"))
                    || (mq[mq.length - 1].equals("1") && mq1.equals("0"))) {
                addSubFlag = true;
            }
            if (addSubFlag) {
                BoothSimulator boothSimulator = new BoothSimulator();
                ac = boothSimulator.sixteenBitAlu(ac, md);
                addSubFlag = false;
                boothSimulator.displayRegistersState(counter);
            }

            // shift case goes everytime.
            String shiftedArrays[][] = rightShiftBy1(ac, mq);
            ac = shiftedArrays[0];
            mq = shiftedArrays[1];
            BoothSimulator boothSimulating = new BoothSimulator();
            boothSimulating.displayRegistersState(counter);

            counter--;
        }

        // Preparing the final product.
        for (int i = 0; i < ac.length; i++) {
            product += ac[i];
        }
        for (int i = 0; i < mq.length; i++) {
            product += mq[i];
        }
        return product;
    }

    /**
     * sixteenBitAlu This method takes in accumulator and multiplicand. Decides
     * either addition or subtraction is to be done. If subtraction then one
     * step for 2's compliment of multiplicand is added. It calls the 1bit Alu
     * for accumulator length times. Also manages the carry in for the 1bit Alu.
     *
     * @param ac2
     * @param md2
     * @return ac2
     */
    public String[] sixteenBitAlu(String[] ac2, String[] md2) {
        String cin = "0";
        BoothSimulator boothSimulator = new BoothSimulator();
        if ((mq[mq.length - 1].equals("1") && mq1.equals("0"))) {
            if (isTwoCompMDCreated) {
                md2 = md2sComp;
            } else {
                md2 = twosCompliment(md2);
            }
        } else {
            md2 = md1;
        }

        // Calling 1bit Alu below
        for (int i = ac2.length - 1; i >= 0; i--) {
            String result[] = boothSimulator.oneBitAlu(cin, ac2[i], md2[i]);
            ac2[i] = result[0];
            cin = result[1];
        }
        return ac2;
    }

    /**
     * oneBitAlu This method takes carry in, accumulator and multiplicand as
     * input. Checks for the different cases of addition and returns the output.
     *
     * @param cin
     * @param ac2
     * @param md2
     * @return output
     */
    public String[] oneBitAlu(String cin, String ac2, String md2) {
        String output[] = new String[2];
        if (ac2.equals("0") && md2.equals("0") && cin.equals("0")) {
            output[0] = "0";
            output[1] = "0";
        } else if (ac2.equals("0") && md2.equals("1") && cin.equals("0")
                || ac2.equals("1") && md2.equals("0") && cin.equals("0")) {
            output[0] = "1";
            output[1] = "0";
        } else if (ac2.equals("1") && md2.equals("1") && cin.equals("0")) {
            output[0] = "0";
            output[1] = "1";
        } else if (ac2.equals("0") && md2.equals("0") && cin.equals("1")) {
            output[0] = "1";
            output[1] = "0";
        } else if (ac2.equals("0") && md2.equals("1") && cin.equals("1")
                || ac2.equals("1") && md2.equals("0") && cin.equals("1")) {
            output[0] = "0";
            output[1] = "1";
        } else if (ac2.equals("1") && md2.equals("1") && cin.equals("1")) {
            output[0] = "1";
            output[1] = "1";
        }
        return output;
    }

    /**
     * twosCompliment This method used for getting 2's compliment of a binary
     * number. It takes in the binary number. Create 1's Compliment and adds 1
     * after that.
     *
     * @param reg
     * @return 2's compliment of binary number
     */
    public String[] twosCompliment(String[] reg) {
        for (int i = 0; i < reg.length; i++) {
            if (reg[i].equals("0")) {
                reg[i] = "1";
            } else {
                reg[i] = "0";
            }
        }
        boolean addCF = true;
        for (int i = reg.length - 1; i >= 0; i--) {
            if (addCF && reg[i].equals("0")) {
                reg[i] = "1";
                addCF = false;
            } else if (addCF && reg[i].equals("1")) {
                reg[i] = "0";
                addCF = true;
            } else if (!addCF && reg[i].equals("0")) {
                reg[i] = "0";
            } else if (!addCF && reg[i].equals("1")) {
                reg[i] = "1";
            }
        }
        if (save2sComp) {
            md2sComp = reg;
            isTwoCompMDCreated = true;
        }
        return reg;
    }

    /**
     * rightShiftBy1 Used for right shift the registers by 1. Takes accumulator
     * and multiplier as input. Right shift both of them in continuation and
     * last bit of multiplier goes to register MQ-1
     *
     * @param ac2
     * @param mq2
     * @return shiftedArrays
     */
    public String[][] rightShiftBy1(String[] ac2, String[] mq2) {
        String amTemp = "0";
        for (int i = 0; i < ac2.length; i++) {
            if (i == 0) {
                ac2[i] = ac2[i];
                amTemp = ac2[i];
            } else {
                String temp = ac2[i];
                ac2[i] = amTemp;
                amTemp = temp;
            }
        }
        for (int i = 0; i < mq2.length; i++) {
            if (i == 0) {
                String temp = mq2[i];
                mq2[i] = amTemp;
                amTemp = temp;
            } else {
                String temp = mq2[i];
                mq2[i] = amTemp;
                amTemp = temp;
            }
        }
        mq1 = amTemp;
        String shiftedArrays[][] = { ac2, mq2 };
        return shiftedArrays;
    }

    /**
     * displayRegistersState Used for displaying the current state of registers
     * and the cycle counter in each cycle.
     *
     * @param counter
     */
    public void displayRegistersState(int counter) {
        // State of registers
        System.out.print(Integer.toBinaryString(0x100 | (counter)).substring(1) + "	");
        for (int i = 0; i < md1.length; i++) {
            System.out.print(md1[i]);
        }
        System.out.print("	");
        for (int i = 0; i < ac.length; i++) {
            System.out.print(ac[i]);
        }
        System.out.print("	");
        for (int i = 0; i < mq.length; i++) {
            System.out.print(mq[i]);
        }
        System.out.print("	");
        System.out.println(mq1);
    }

}