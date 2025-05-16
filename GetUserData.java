import java.io.*;
import java.util.Scanner;



public class GetUserData extends BinaryDataConverter {
    private String getinputF;
    private char BorderType;
    private char DataType;
    private int sizeData;


    public void getInput() {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter the file name");
        getinputF=scanner.nextLine();
        System.out.println("Enter the byte ordering type (l or b)");
        BorderType=scanner.next().charAt(0);;
        System.out.println("Enter the data type to be converted(i:intiger , u: unsigned ,f: floating)" );
        DataType=scanner.next().charAt(0);
        System.out.println("Enter the size of the given data type(1,2,3 or 4)");
        sizeData=scanner.nextInt();

    }
    public void displayData() {
        System.out.println("File Name: " + getinputF);
        if(BorderType=='l'){
            System.out.println("Byte Order Type: Little Endian" );
        }
        else if (BorderType=='b') {
            System.out.println("Byte Order Type: Big Endian " );
        }
        else System.out.println("Byte Order Type is invalid ");

        if(DataType=='i'){System.out.println("Data Type: Integer Number ");

        } else if (DataType=='u') {System.out.println("Data Type: Unsigned Number ");

        } else if (DataType=='f') {System.out.println("Data Type: Floating point number ");

        }else System.out.println("Data Type is invalid ");

        if(sizeData==1){System.out.println("Size of Data: 1 byte " );

        } else if (sizeData==2) {  System.out.println("Size of Data: 2 bytes " );

        } else if (sizeData==3) {System.out.println("Size of Data: 3 bytes" );

        } else if (sizeData==4) {System.out.println("Size of Data: 4 bytes " );

        }else System.out.println("Size of Data is invalid " );

    }

    public StringBuilder readFileData()  {
        StringBuilder outputOrderedConverted = new StringBuilder();
        File inputF;
        try{
            inputF = new File(getinputF);

            BufferedReader reader=new BufferedReader(new FileReader(inputF));
            String line;
            String WithoutSpace;

            while((line= reader.readLine())!=null){ // read line by line


                WithoutSpace=line.replace(" ","");  // removes the spaces in the line

                if(BorderType=='l') { // If it's little endian, it first splits the data into the required number of hex pairs based on the desired size, stores them in an array, and then reverses the order.
                    WithoutSpace="";

                    String[] hexSplitted = line.split(" ");
                    for (int i = 0; i < 12; i += sizeData) {


                        for (int j =i+sizeData-1; j >= i; j--) {
                            WithoutSpace += hexSplitted[j];
                        }


                    }
                }


                int[] number=new int[WithoutSpace.length()];
                String binary="";
                for (int i = 0; i < (12/sizeData); i++) {

                    for ( int j = 0; j <sizeData*2 ; j++) {

                        number[j]=Integer.parseInt(String.valueOf(WithoutSpace.charAt(j)),16); //It takes each element of the line individually as hex and converts it to an integer
                        binary += String.format("%4s", Integer.toBinaryString(number[j])).replace(' ', '0'); // Integers are converted to binary, each one being 4 bits

                    }
                    StringBuffer stringBuffer=new StringBuffer(WithoutSpace);
                    stringBuffer.delete(0,(sizeData*2));
                    WithoutSpace= String.valueOf(stringBuffer);

                    outputOrderedConverted.append(binary);
                    outputOrderedConverted.append(" ");
                    binary=""; //reset for the loop



                }

                outputOrderedConverted.append("\n");


            }
            reader.close();

        }  catch (IOException e) {
            System.out.println("File not found: ");
        }
        StringBuilder finalOutput = new StringBuilder();
        String[] BinaryData = outputOrderedConverted.toString().split("\n");


        for (String line : BinaryData) {
            String[] binaryData = line.split(" ");

            if (DataType == 'i') {
                for (String binary : binaryData) {
                    finalOutput.append(BinaryToSigned(binary)).append(" ");
                }
            } else if (DataType == 'u') {
                for (String binary : binaryData) {
                    finalOutput.append(BinaryToUnsigned(binary)).append(" ");
                }
            } else if (DataType == 'f') {
                for (String binary : binaryData) {
                    finalOutput.append(BinaryToFloatingPointNumber(binary, sizeData)).append(" ");
                }
            }

            finalOutput.append("\n");
        }
        try (FileWriter writer = new FileWriter("output.txt")) {
            writer.write(finalOutput.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("The output.txt file has been successfully created.");


        return finalOutput;
    }


    public char getBorderType() {
        return BorderType;
    }
    public char getDataType() {
        return DataType;
    }
    public int getSizeData() {
        return sizeData;
    }


    public String getGetinputF() {
        return getinputF;
    }
}