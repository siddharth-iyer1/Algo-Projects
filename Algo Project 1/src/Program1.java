/*
 * Name: Siddharth Iyer
 * EID: ssi325
 */

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.LinkedList;
 
 /**
  * Your solution goes in this class.
  *
  * Please do not modify the other files we have provided for you, as we will use
  * our own versions of those files when grading your project. You are
  * responsible for ensuring that your solution works with the original version
  * of all the other files we have provided for you.
  *
  * That said, please feel free to add additional files and classes to your
  * solution, as you see fit. We will use ALL of your additional files when
  * grading your solution. However, do not add extra import statements to this file.
  */
 public class Program1 extends AbstractProgram1 {

     /**
      * Determines whether a candidate Matching represents a solution to the stable matching problem.
      * Study the description of a Matching in the project documentation to help you with this.
      */
     @Override
     public boolean isStableMatching(Matching problem) {
         ArrayList<Integer> donor_matching = problem.getDonorMatching();
         ArrayList<ArrayList<Integer>> hospital_pref = problem.getHospitalPreference();
         ArrayList<ArrayList<Integer>> donor_pref = problem.getDonorPreference();

         for (int i = 0; i < donor_matching.size(); i++) {
             int curr_hospital = donor_matching.get(i);
             if (curr_hospital != -1) {
                 for (int j = 0; j < problem.getDonorCount(); j++) {
                     if (donor_matching.get(j) == -1) {
                         for (int k = 0; k < hospital_pref.get(curr_hospital).size(); k++) {
                             if (hospital_pref.get(curr_hospital).get(k) == j) {
                                 return false;
                             }
                             if (hospital_pref.get(curr_hospital).get(k) == i) {
                                 break;
                             }
                         }
                     }
                 }
             }
         }

         for (int i = 0; i < donor_matching.size(); i++) {
             int hospital1 = donor_matching.get(i);
             if (hospital1 != -1) {
                 for (int j = 0; j < problem.getDonorCount(); j++) {
                     int hospital2 = donor_matching.get(j);
                     if (hospital2 != -1 && hospital1 != hospital2) {
                         int iRank1 = -1;
                         int iRank2 = -1;
                         int jRank1 = -1;
                         int jRank2 = -1;
                         for (int k = 0; k < hospital_pref.get(hospital1).size(); k++) {
                             if (hospital_pref.get(hospital1).get(k) == i) {
                                 iRank1 = k;
                             }
                             if (hospital_pref.get(hospital1).get(k) == j) {
                                 jRank1 = k;
                             }
                         }
                         for (int k = 0; k < hospital_pref.get(hospital2).size(); k++) {
                             if (hospital_pref.get(hospital2).get(k) == i) {
                                 iRank2 = k;
                             }
                             if (hospital_pref.get(hospital2).get(k) == j) {
                                 jRank2 = k;
                             }
                         }
                         if (iRank1 > jRank1 && jRank2 > iRank2) {
                             return false;
                         }
                     }
                 }
             }
         }
         return true;
     }

     /**
      * Determines a solution to the stable matching problem from the given input set. Study the
      * project description to understand the variables which represent the input to your solution.
      *
      * @return A stable Matching.
      */
     @Override
     public Matching stableMatchingGaleShapley_hospitaloptimal(Matching problem) {
         // Set every matching in Donor ArrayList to -1
         ArrayList<Integer> donor_matching = new ArrayList<Integer>();
         for(int i = 0; i < problem.getDonorCount(); i++){
             donor_matching.add(i, -1);
         }

         ArrayList<Integer> openings = problem.getHospitalPositions();
         ArrayList<ArrayList<Integer>> hospital_pref = problem.getHospitalPreference();
         ArrayList<ArrayList<Integer>> donor_pref = problem.getDonorPreference();

         // Find first hospital with >0 openings
         for(int i = 0; i < openings.size(); i++) {
             if (openings.get(i) > 0) {
                 // Make offers for all the free openings in the hospital
                 while (openings.get(i) > 0) {
                     int first_pref = hospital_pref.get(i).get(0);  // Highest preferred at the moment
                     if (donor_matching.get(first_pref).equals(-1)) { // If unmatched
                         donor_matching.set(first_pref, i);         // Match donor to the current hospital
                         openings.set(i, (openings.get(i) - 1));
                         hospital_pref.get(i).remove(0);
                     } else {
                         int current_hospital_rank = 0;
                         int current_match_rank = 0;
                         for (int k = 0; k < openings.size(); k++) {  // Iterates through number of hospitals
                             if (donor_pref.get(first_pref).get(k).equals(i)) {
                                 current_hospital_rank = k;
                             }
                             if (donor_pref.get(first_pref).get(k).equals(donor_matching.get(first_pref))) {
                                 current_match_rank = k;
                             }
                         }
                         if (current_hospital_rank < current_match_rank) {    // Swap
                             openings.set(donor_matching.get(first_pref), openings.get(donor_matching.get(first_pref)) + 1);
                             donor_matching.set(first_pref, i);         // Match donor to the current hospital
                             openings.set(i, (openings.get(i) - 1));
                             hospital_pref.get(i).remove(0);
                             i = 0;
                         } else {  // Failed proposal
                             hospital_pref.get(i).remove(0);
                         }
                     }
                 }
             }
         }
         problem.setDonorMatching(donor_matching);
         return problem;
     }

     /**
      * Determines a solution to the stable matching problem from the given input set. Study the
      * project description to understand the variables which represent the input to your solution.
      *
      * @return A stable Matching.
      */
     @Override
     public Matching stableMatchingGaleShapley_donoroptimal(Matching problem) {
         ArrayList<Integer> donor_matching = new ArrayList<Integer>();
         for(int i = 0; i < problem.getDonorCount(); i++){
             donor_matching.add(i, -1);
         }

         ArrayList<Integer> openings = problem.getHospitalPositions();
         ArrayList<ArrayList<Integer>> hospital_pref = problem.getHospitalPreference();
         ArrayList<ArrayList<Integer>> donor_pref = problem.getDonorPreference();

         ArrayList<ArrayList<Integer>> inverse_pref_list = new ArrayList<ArrayList<Integer>>();
         for(int i = 0; i < problem.getHospitalCount(); i++){
             ArrayList<Integer> aniketh = new ArrayList<>();
             for(int j = 0; j < problem.getDonorCount(); j++){
                 aniketh.add( -1);
             }
             inverse_pref_list.add(aniketh);
         }

         for(int i = 0; i < problem.getHospitalCount(); i++){
             for(int j = 0; j < problem.getDonorCount(); j++){
                 int pref = hospital_pref.get(i).get(j);
                 inverse_pref_list.get(i).set(pref, j);
             }
         }
//         System.out.println(inverse_pref_list);
         System.out.println(donor_pref);
         System.out.println(hospital_pref);

         for(int j = 0; j < problem.getDonorCount(); j++) {
             if (!(donor_pref.get(j).size() == 0)) {
                 if (donor_matching.get(j).equals(-1)) {
                     int highest_pref = donor_pref.get(j).get(0);
                     if (openings.get(highest_pref) > 0) {
                         donor_matching.set(j, highest_pref);
                         openings.set(highest_pref, (openings.get(highest_pref) - 1));
                         donor_pref.get(j).remove(0);
                         j = -1;
                     } else {
                         // Find least preferred donor in hospital
                         int least_pref = 0;
                         int least_pref_rank = -1;
                         for (int k = 0; k < problem.getDonorCount(); k++) {
                             if (donor_matching.get(k).equals(highest_pref)) {
                                 if (inverse_pref_list.get(highest_pref).get(k) > least_pref_rank) {
                                     least_pref = k;
                                     least_pref_rank = inverse_pref_list.get(highest_pref).get(k);
                                 }
                             }
                         }
                         // Now if current donor is more preferred by the hospital
                         if (inverse_pref_list.get(highest_pref).get(j) < least_pref_rank) {
                             donor_matching.set(j, highest_pref);
                             donor_matching.set(least_pref, -1);
                             donor_pref.get(j).remove(0);
                             j = -1;
                         } else {
                             // Failed proposal
                             donor_pref.get(j).remove(0);
                             j = -1;
                         }
                     }
                 }
             }
         }
         problem.setDonorMatching(donor_matching);
         return problem;
     }
 }