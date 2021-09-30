package AuxiliaryPackage;

import CARL.entities.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AuxiliaryPackageUtils {

    public static List<User> addUsers() {

        String line = "";
        String splitBy = ",";
        List<User> userArrayList = new ArrayList<>();
        try {
            //parsing a CSV file into BufferedReader class constructor
            Path absolutePath = Paths.get(".").toAbsolutePath().normalize();
            String carlCloudTokensFilePath = absolutePath.toFile().getAbsolutePath() + "\\src\\main\\resources\\CARL\\CARL_cloud_tokens.csv";
            BufferedReader br = new BufferedReader(new FileReader(carlCloudTokensFilePath));
            br.readLine();
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                // use comma as separator
                String[] userInfo = line.split(splitBy);
                userArrayList.add(new User(userInfo[1], userInfo[2], Integer.parseInt(userInfo[0])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //User user = userArrayList.get(4);
        //userArrayList = new ArrayList<>(){{add(user); }};

        // THE NEXT LINES ARE GOING TO BE CHANGED. THE USER THRESHOLDS FOR EACH USER WILL BE READ FROM AN EXTERNAL FILE IN THE FUTURE.
        // THE NEXT BLOCK CODE IS USED ONLY FOR TESTING PURPOSES, THAT'S WHY IT IS HARDCODED.
        userArrayList.get(0).getUserThresholds().setFatBurnZone(50);
        userArrayList.get(0).getUserThresholds().setInactivity_Duration(50);
        userArrayList.get(0).getUserThresholds().setStressOrPainDuration(60);
        userArrayList.get(0).getUserThresholds().setIncreasedNapping_asleepInNaps(50);
        userArrayList.get(0).getUserThresholds().setLackOfExercise_Duration(50);
        userArrayList.get(0).getUserThresholds().setInsomnia_TimeToFallAsleep(50);
        userArrayList.get(0).getUserThresholds().setSteps_Low(50);
        userArrayList.get(0).getUserThresholds().setSteps_Medium(70);
        userArrayList.get(0).getUserThresholds().setRestlessness_NumberOfInterruptionsInADay(50);

        return userArrayList;

    }
}

