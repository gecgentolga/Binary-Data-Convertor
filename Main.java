import java.io.IOException;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        GetUserData getUserData=new GetUserData();
        getUserData.getInput();
        getUserData.displayData();
        getUserData.readFileData();



    }
}
