package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRSServer.Course;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private or public fields and methods to this class as you see fit.
 */
public class Database {
    private ArrayList<Course> courseArrayList;
	private ConcurrentHashMap<Short, Course> CoursesList;
	//private ConcurrentHashMap<String, String> userNameAndPassword;
	private ConcurrentHashMap<String, User_Data> users;
  private Object LockchikKDAMforStudent;
	private static class SingltonHolder {
		private static Database instance = new Database();
	}

	//to prevent user from creating new Database
	private Database() {
		LockchikKDAMforStudent =new Object();
		users=new ConcurrentHashMap<>();
		CoursesList = new ConcurrentHashMap<>();
		courseArrayList=new ArrayList<>();
		// TODO: implement
	}
	public List<String> StudentRegistedtoThisCourse(Short courseNam) {
		synchronized (users) {
			List<String> studentRegisted = new ArrayList<>();
			for (String key : users.keySet()) {
				if (users.get(key).getUserCourses().contains(courseNam))
					studentRegisted.add(users.get(key).getUserName());
			}
			List<String> sortedNames = studentRegisted.stream().sorted().collect(Collectors.toList());
			return studentRegisted;
		}
	}

	/**
	 *
	 *
	 * @param UserNam : student  user name
	 * @param CourseNum: number of course we want to chick if student did all the kdam for it
	 * @return
	 */
    public Boolean chikKDAMforStudent(String UserNam,Short CourseNum){
		synchronized (LockchikKDAMforStudent) {
			List<Short> UserCourse = users.get(UserNam).getUserCourses();
			List<Short> KdamCourse =CoursesList.get(CourseNum).getKdamCoursesList();
			for (int i = 0; i < KdamCourse.size(); i++) {
				if (!UserCourse.contains(KdamCourse.get(i))) {
					return false;
				}
			}
			return true;
		}
}
	public void AddUser(User_Data newUser) {
		synchronized (users) {
			users.put(newUser.getUserName(), newUser);
		}
	}
	public Boolean chickUser(String userName){
		if(users.containsKey(userName)){
			return true;
		}
		return false;
	}
	public User_Data getUser(String userNam){
		return users.get(userNam);
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return SingltonHolder.instance;
	}

	/**
	 * loades the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		Boolean success = true;
		try (Scanner coursesFile = new Scanner(new FileReader(coursesFilePath))){
			String line;
			Course course;
			while (coursesFile.hasNext()) {
				 line=coursesFile.nextLine();
				 String lineSplit;
				 course = new Course();
				 lineSplit = line;
				 String[] CourseInfo = lineSplit.split("\\|");
				 course.setCourseNum(Short.parseShort(CourseInfo[0]));
				 course.setCourseName(CourseInfo[1]);
                   /*
                   we want to get courses num from the txt ,if the length =2 then we had empty array
                   if not we char every word and then split between ","
                    */
				ArrayList<String> stringList = new ArrayList<>();
				if (CourseInfo[2].length() > 2) {
					char[] CourseKdam = CourseInfo[2].toCharArray();
					String courseNum = null;
					for (int i = 1; i < CourseKdam.length - 1; i++) {
						if (CourseKdam[i] != ',') {
							if (courseNum == null) {
								courseNum = String.valueOf(CourseKdam[i]);
							} else {
								courseNum = courseNum+(CourseKdam[i]);
							}
						}
						if (CourseKdam[i] == ',' || i == CourseKdam.length - 2) {
							//i++;
							stringList.add(courseNum);
							courseNum = null;
						}
					}
					ArrayList<Short> ShortList = new ArrayList<>();
					for (int i = 0; i <stringList.size(); i++) {
						ShortList.add(Short.parseShort(stringList.get(i)));
						System.out.println(ShortList.get(i)+" course num ");
					}
					course.setKdamCoursesList(ShortList);
				} else {
					course.setKdamCoursesList(null);
					System.out.println("null");
				}
				course.setNumOfMaxStudents(Integer.parseInt(CourseInfo[3]));
				System.out.println(course.getCourseNum());
				courseArrayList.add(course);
				CoursesList.put(course.getCourseNum(), course);

			}
		} catch (FileNotFoundException e) {
			System.out.println("Could net read the coursesFile at Database");
			success = false;
		}
		// TODO: implement
		return success;
	}
public String getCourseName(Short CourseNum){
		return CoursesList.get(CourseNum).getCourseName();
}

//this func will sort coursesList at the same order  at courses list
public List<Short> sortedList(List<Short> coursesList) {
	synchronized (courseArrayList) {
		List<Short> sortedCourse = new ArrayList<>();
		List<Short> coursesNum = new ArrayList<>();
		for (int i = 0; i < courseArrayList.size(); i++) {
			coursesNum.add(courseArrayList.get(i).getCourseNum());
		}
		for (int i = 0; i < coursesNum.size(); i++) {
			if (coursesList.contains(coursesNum.get(i))) {
				sortedCourse.add(coursesNum.get(i));
			}
		}
		return sortedCourse;
	}
}
	public ConcurrentHashMap<Short, Course> getCoursesList() {
		return CoursesList;
	}

	public ArrayList<Course> getCoursesArrayList() {
		return courseArrayList;
	}

	//public String getUserPassword(String User){
	//	return userNameAndPassword.get(User);
	//}

//	public boolean addUserPassword(String UserNam,String UserPasword){
//		synchronized (userNameAndPassword) {
//			if (!(userNameAndPassword.containsKey(UserNam))) {
//				userNameAndPassword.put(UserNam, UserPasword);
//				return true;
//			}
//			return false;
//		}
//	}
}
