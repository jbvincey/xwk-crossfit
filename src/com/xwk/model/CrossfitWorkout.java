package com.xwk.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.model.Exercise.ExerciseType;

public class CrossfitWorkout {

	private long id;
	private String name;
	private String description;
	private String types;
	private List<Exercise> exercises;
	private List<CrossfitPerformance> performances;
	private int sets = 1;


	public CrossfitWorkout() {
		exercises = new ArrayList<Exercise>();
		performances = new ArrayList<CrossfitPerformance>();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Exercise> getExercises() {
		return exercises;
	}
	public void addExercise(Exercise exercise) {
		this.exercises.add(exercise);
	}
	public List<CrossfitPerformance> getPerformances() {
		return performances;
	}
	public void addCrossfitPerformance(CrossfitPerformance performance) {
		this.performances.add(performance);
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}


	public int getSets() {
		return sets;
	}

	public void setSets(int sets) {
		this.sets = sets;
	}

	public static CrossfitWorkout recoverCrossfitWorkout(Bundle bundle, CrossfitWorkoutDAO crossfitWorkoutDAO) {
		CrossfitWorkout crossfitWorkout = null;
		if(bundle!=null && bundle.containsKey(Ids.CROSSFIT_WORKOUT_ID)) {
			crossfitWorkout = crossfitWorkoutDAO.getCrossfitWorkout(bundle.getLong(Ids.CROSSFIT_WORKOUT_ID));
		}
		return crossfitWorkout;
	}

	/* Initialisation de la base de donnée avec les exercices et workouts */
	public static void loadDefaultWorkouts(CrossfitWorkoutDAO crossfitWorkoutDAO, ExerciseDAO exerciseDAO, Context context) {

		BitmapFactory.Options options = new BitmapFactory.Options();

		/* Legs Exercises*/

		//SQUAT
		Exercise squat = new Exercise();
		squat.setName("Squat");
		squat.setDescription("Stand with the feet slightly turned out. Crouch by bending the knees until the thighs are parallel to the floor. " +
				"The heels should not rise off the floor. Press through the heels to return to a standing position.");
		squat.setType(ExerciseType.legs);
		Bitmap pic1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.squat, options);
		ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
		pic1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
		pic1.recycle();
		byte[] byteArray1 = stream1.toByteArray();
		try {
			stream1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		squat.setImage(byteArray1);
		squat.setId(exerciseDAO.addExercise(squat));

		//BURPEES
		Exercise burpees = new Exercise();
		burpees.setName("Burpees");
		burpees.setDescription("Stand with the feet slightly turned out. Crouch like for a squat. Once the thighs are parallel " +
				"to the floor, put the hands on the floor and kick the feet " +
				"back to a push-up position. Complete one push-up, then return the feet to the squat position, and stand up.");
		burpees.setType(ExerciseType.legs);
		Bitmap pic2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.burpee, options);
		ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
		pic2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
		pic2.recycle();
		byte[] byteArray2 = stream2.toByteArray();
		try {
			stream2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		burpees.setImage(byteArray2);
		burpees.setId(exerciseDAO.addExercise(burpees));

		//JUMPING LUNGE
		Exercise jumpingLunge = new Exercise();
		jumpingLunge.setName("Jumping lunge");
		jumpingLunge.setDescription("Stand with the feet together. Lunge forward with the right foot. Jump" +
				" straight up, switch legs while in the air. Land in lunge position with your left leg forward. Jump straight up...");
		jumpingLunge.setType(ExerciseType.legs);
		Bitmap pic3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.jumpinglunge, options);
		ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
		pic3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
		pic3.recycle();
		byte[] byteArray3 = stream3.toByteArray();
		try {
			stream3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jumpingLunge.setImage(byteArray3);
		jumpingLunge.setId(exerciseDAO.addExercise(jumpingLunge));

		//PISTOL SQUAT
		Exercise pistolSquat = new Exercise();
		pistolSquat.setName("Pistol squat");
		pistolSquat.setDescription("Stand holding the arms straight out in front" +
				" of the body, and raise the right leg. Complete a squat with the right leg raised, " +
				"then return to standing. Swap legs in the middle of the set.");
		pistolSquat.setType(ExerciseType.legs);
		Bitmap pic4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pistolsquat, options);
		ByteArrayOutputStream stream4 = new ByteArrayOutputStream();
		pic4.compress(Bitmap.CompressFormat.JPEG, 100, stream4);
		pic4.recycle();
		byte[] byteArray4 = stream4.toByteArray();
		try {
			stream4.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pistolSquat.setImage(byteArray4);
		pistolSquat.setId(exerciseDAO.addExercise(pistolSquat));

		//CALF RAISE
		Exercise calfRaise = new Exercise();
		calfRaise.setName("Calf raise");
		calfRaise.setDescription("Stand at the edge of a step or something elevated. " +
				"Rise up on the toes, hold briefly, and come back down.");
		calfRaise.setType(ExerciseType.legs);
		Bitmap pic5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.calfraise, options);
		ByteArrayOutputStream stream5 = new ByteArrayOutputStream();
		pic5.compress(Bitmap.CompressFormat.JPEG, 100, stream5);
		pic5.recycle();
		byte[] byteArray5 = stream5.toByteArray();
		try {
			stream5.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calfRaise.setImage(byteArray5);
		calfRaise.setId(exerciseDAO.addExercise(calfRaise));

		//MOUNTAIN CLIMBER
		Exercise mountainClimber = new Exercise();
		mountainClimber.setName("Moutain climber");
		mountainClimber.setDescription("Get into push-up position. Bring your left leg " +
				"forward under your chest, and stretch your right leg backward. Swap legs, the hands stay on the floor. ");
		mountainClimber.setType(ExerciseType.legs);
		Bitmap pic6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.moutainclimber, options);
		ByteArrayOutputStream stream6 = new ByteArrayOutputStream();
		pic6.compress(Bitmap.CompressFormat.JPEG, 100, stream6);
		pic6.recycle();
		byte[] byteArray6 = stream6.toByteArray();
		try {
			stream6.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mountainClimber.setImage(byteArray6);
		mountainClimber.setId(exerciseDAO.addExercise(mountainClimber));

		//TUCK JUMP
		Exercise tuckJump = new Exercise();
		tuckJump.setName("Tuck jump");
		tuckJump.setDescription("Stand with the knees slightly bent. Jump up as high as possible, " +
				"bring the knees to your chest.");
		tuckJump.setType(ExerciseType.legs);
		Bitmap pic7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tuckjump, options);
		ByteArrayOutputStream stream7 = new ByteArrayOutputStream();
		pic7.compress(Bitmap.CompressFormat.JPEG, 100, stream7);
		pic7.recycle();
		byte[] byteArray7 = stream7.toByteArray();
		try {
			stream7.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tuckJump.setImage(byteArray7);
		tuckJump.setId(exerciseDAO.addExercise(tuckJump));

		//WALL SIT
		Exercise wallSit = new Exercise();
		wallSit.setName("Wall sit");
		wallSit.setDescription("Put your back against a wall, slide your back down until the thighs are parallel to the ground. " +
				"Knees should be exactly above the ankles. Keep the back straight and stand, consider 1 rep as 1 minute.");
		wallSit.setType(ExerciseType.legs);
		Bitmap pic8 = BitmapFactory.decodeResource(context.getResources(), R.drawable.wallsit, options);
		ByteArrayOutputStream stream8 = new ByteArrayOutputStream();
		pic8.compress(Bitmap.CompressFormat.JPEG, 100, stream8);
		pic8.recycle();
		byte[] byteArray8 = stream8.toByteArray();
		try {
			stream8.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wallSit.setImage(byteArray8);
		wallSit.setId(exerciseDAO.addExercise(wallSit));



		/* arms Exercises*/

		//DIAMOND PUSHUP
		Exercise diamondPushup = new Exercise();
		diamondPushup.setName("Diamond push-up");
		diamondPushup.setDescription("Starting with a push-up position, put your " +
				"hands close to eachother in order to make the shape of a diamond. Complete a push-up. Triceps should burn.");
		diamondPushup.setType(ExerciseType.arms);
		Bitmap pic9 = BitmapFactory.decodeResource(context.getResources(), R.drawable.diamondpushup, options);
		ByteArrayOutputStream stream9 = new ByteArrayOutputStream();
		pic9.compress(Bitmap.CompressFormat.JPEG, 100, stream9);
		pic9.recycle();
		byte[] byteArray9 = stream9.toByteArray();
		try {
			stream9.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		diamondPushup.setImage(byteArray9);
		diamondPushup.setId(exerciseDAO.addExercise(diamondPushup));

		//DIPS
		Exercise dips = new Exercise();
		dips.setName("Dips");
		dips.setDescription("Grab the edge of a chair with your hands, straighten your arms, and put your feet at the edge of another " +
				"chair. Lower your body by bending the arms to a 90-degree angle. Push back to straighten your arms again.");
		dips.setType(ExerciseType.arms);
		Bitmap pic10 = BitmapFactory.decodeResource(context.getResources(), R.drawable.dips, options);
		ByteArrayOutputStream stream10 = new ByteArrayOutputStream();
		pic10.compress(Bitmap.CompressFormat.JPEG, 100, stream10);
		pic10.recycle();
		byte[] byteArray10 = stream10.toByteArray();
		try {
			stream10.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dips.setImage(byteArray10);
		dips.setId(exerciseDAO.addExercise(dips));

		//BOXER
		Exercise boxer = new Exercise();
		boxer.setName("Boxer");
		boxer.setDescription("Start with the feet hip-width apart " +
				"and knees slightly bent, keep the elbows in. Extend one arm forward. Pull back your arm and switch.");
		boxer.setType(ExerciseType.arms);
		Bitmap pic11 = BitmapFactory.decodeResource(context.getResources(), R.drawable.boxer, options);
		ByteArrayOutputStream stream11 = new ByteArrayOutputStream();
		pic11.compress(Bitmap.CompressFormat.JPEG, 100, stream11);
		pic11.recycle();
		byte[] byteArray11 = stream11.toByteArray();
		try {
			stream11.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boxer.setImage(byteArray11);
		boxer.setId(exerciseDAO.addExercise(boxer));

		//ARM CIRCLES
		Exercise armCircles = new Exercise();
		armCircles.setName("Arm circles");
		armCircles.setDescription("Stand with arms extended by the sides parallel to the floor. Make clockwise" +
				" circles for half the set, then counter-clockwise for the rest. Possibly add some weights in your hand.");
		armCircles.setType(ExerciseType.arms);
		Bitmap pic12 = BitmapFactory.decodeResource(context.getResources(), R.drawable.circles, options);
		ByteArrayOutputStream stream12 = new ByteArrayOutputStream();
		pic12.compress(Bitmap.CompressFormat.JPEG, 100, stream12);
		pic12.recycle();
		byte[] byteArray12 = stream12.toByteArray();
		try {
			stream12.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		armCircles.setImage(byteArray12);
		armCircles.setId(exerciseDAO.addExercise(armCircles));

		//SHOULDER TAPS
		/*Exercise shoulderTaps = new Exercise();
		shoulderTaps.setName("Shoulder taps");
		shoulderTaps.setDescription("Start with push-up position (arms straight). Tap " +
				"on your left shoulder with your right hand, then on your right shoulder with your left hand.");
		shoulderTaps.setType(ExerciseType.arms);
		Bitmap pic13 = BitmapFactory.decodeResource(context.getResources(), R.drawable.shouldertap, options);
		ByteArrayOutputStream stream13 = new ByteArrayOutputStream();
		pic13.compress(Bitmap.CompressFormat.JPEG, 100, stream13);
		pic13.recycle();
		byte[] byteArray13 = stream13.toByteArray();
		try {
			stream13.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shoulderTaps.setImage(byteArray13);
		shoulderTaps.setId(exerciseDAO.addExercise(shoulderTaps));*/

		//TRICEPS EXTENSIONS
		Exercise tricepsExtensions = new Exercise();
		tricepsExtensions.setName("Triceps extensions");
		tricepsExtensions.setDescription("Start with a push-up position, put your elbows on the floor to have them under your shoulders. Your arms should make" +
				" a 90-degree angle. Straighten your arms raising up your elbows, then put back your elbows on the floor.");
		tricepsExtensions.setType(ExerciseType.arms);
		Bitmap pic14 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tricepsextension2, options);
		ByteArrayOutputStream stream14 = new ByteArrayOutputStream();
		pic14.compress(Bitmap.CompressFormat.JPEG, 100, stream14);
		pic14.recycle();
		byte[] byteArray14 = stream14.toByteArray();
		try {
			stream14.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tricepsExtensions.setImage(byteArray14);
		tricepsExtensions.setId(exerciseDAO.addExercise(tricepsExtensions));

		//ONE ARM PUSHUP
		Exercise oneArmPushup = new Exercise();
		oneArmPushup.setName("One arm push-up");
		oneArmPushup.setDescription("Feet on the floor, legs straight, put one hand to your chest level on the floor, the other hand behind your back. " +
				"Lower your chest to the floor, push up to straighten your arm back. Switch arms in the middle of the set.");
		oneArmPushup.setType(ExerciseType.arms);
		Bitmap pic15 = BitmapFactory.decodeResource(context.getResources(), R.drawable.onearmpushup, options);
		ByteArrayOutputStream stream15 = new ByteArrayOutputStream();
		pic15.compress(Bitmap.CompressFormat.JPEG, 100, stream15);
		pic15.recycle();
		byte[] byteArray15 = stream15.toByteArray();
		try {
			stream15.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oneArmPushup.setImage(byteArray15);
		oneArmPushup.setId(exerciseDAO.addExercise(oneArmPushup));


		/* chest Exercises*/

		//PUSHUP
		Exercise pushup = new Exercise();
		pushup.setName("Push-up");
		pushup.setDescription("Feet on the floor, legs straight, hands on the floor to the shoulders' " +
				"level, arms straight. Lower your chest to the floor, push up to straighten your arms back.");
		pushup.setType(ExerciseType.chest);
		Bitmap pic16 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pushup, options);
		ByteArrayOutputStream stream16 = new ByteArrayOutputStream();
		pic16.compress(Bitmap.CompressFormat.JPEG, 100, stream16);
		pic16.recycle();
		byte[] byteArray16 = stream16.toByteArray();
		try {
			stream16.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pushup.setImage(byteArray16);
		pushup.setId(exerciseDAO.addExercise(pushup));

		//CLAPPING PUSHUP
		Exercise clappingPushup = new Exercise();
		clappingPushup.setName("Clapping push-up");
		clappingPushup.setDescription("Feet on the floor, legs straight, hands on the floor to the shoulders level, arms straight. " +
				"Lower your chest to the floor, push up vigorously to take your hands off the floor, clap your hands and put it back on the floor.");
		clappingPushup.setType(ExerciseType.chest);
		Bitmap pic17 = BitmapFactory.decodeResource(context.getResources(), R.drawable.clappingpushup, options);
		ByteArrayOutputStream stream17 = new ByteArrayOutputStream();
		pic17.compress(Bitmap.CompressFormat.JPEG, 100, stream17);
		pic17.recycle();
		byte[] byteArray17 = stream17.toByteArray();
		try {
			stream17.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clappingPushup.setImage(byteArray17);
		clappingPushup.setId(exerciseDAO.addExercise(clappingPushup));

		//WIDE GRIP PUSHUP
		Exercise wideGripPushup = new Exercise();
		wideGripPushup.setName("Wide grip push-up");
		wideGripPushup.setDescription("Feet on the floor, legs straight, hands on the floor spread wider " +
				"than shoulder length, arms straight. Lower your chest to the floor, push up to straighten your arms back.");
		wideGripPushup.setType(ExerciseType.chest);
		Bitmap pic18 = BitmapFactory.decodeResource(context.getResources(), R.drawable.widegrippushup, options);
		ByteArrayOutputStream stream18 = new ByteArrayOutputStream();
		pic18.compress(Bitmap.CompressFormat.JPEG, 100, stream18);
		pic18.recycle();
		byte[] byteArray18 = stream18.toByteArray();
		try {
			stream18.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wideGripPushup.setImage(byteArray18);
		wideGripPushup.setId(exerciseDAO.addExercise(wideGripPushup));

		//FEET ELEVATED PUSHUP
		Exercise feetElevatedPushup = new Exercise();
		feetElevatedPushup.setName("Feet elevated push-up");
		feetElevatedPushup.setDescription("Put your feet on a step, bench, chair, or something elevated, hands on the floor " +
				"to the shoulders level, arms straight. Lower your chest to the floor, push up to straighten your arms back.");
		feetElevatedPushup.setType(ExerciseType.chest);
		Bitmap pic19 = BitmapFactory.decodeResource(context.getResources(), R.drawable.elevatedpushup, options);
		ByteArrayOutputStream stream19 = new ByteArrayOutputStream();
		pic19.compress(Bitmap.CompressFormat.JPEG, 100, stream19);
		pic19.recycle();
		byte[] byteArray19 = stream19.toByteArray();
		try {
			stream19.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		feetElevatedPushup.setImage(byteArray19);
		feetElevatedPushup.setId(exerciseDAO.addExercise(feetElevatedPushup));

		//SINGLE LEG PUSHUP
		Exercise singleLegPushup = new Exercise();
		singleLegPushup.setName("Single leg push-up");
		singleLegPushup.setDescription("Put one foot on the floor, lift the other leg off the ground, hands on the floor " +
				"to the shoulders level, arms straight. Lower your chest to the floor, push up to straighten your arms back." +
				"Swap legs in the middle of the set.");
		singleLegPushup.setType(ExerciseType.chest);
		Bitmap pic20 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pushupraisedleg, options);
		ByteArrayOutputStream stream20 = new ByteArrayOutputStream();
		pic20.compress(Bitmap.CompressFormat.JPEG, 100, stream20);
		pic20.recycle();
		byte[] byteArray20 = stream20.toByteArray();
		try {
			stream20.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		singleLegPushup.setImage(byteArray20);
		singleLegPushup.setId(exerciseDAO.addExercise(singleLegPushup));


		//ECCENTRIC PUSHUP
		Exercise eccentricPushup = new Exercise();
		eccentricPushup.setName("Eccentric push-up");
		eccentricPushup.setDescription("Feet on the floor, legs straight, hands on the floor to the shoulders level, " +
				"arms straight. Lower slowly your chest to the floor (4/5 seconds), raise up normally.");
		eccentricPushup.setType(ExerciseType.chest);
		Bitmap pic21 = BitmapFactory.decodeResource(context.getResources(), R.drawable.eccentricpushup, options);
		ByteArrayOutputStream stream21 = new ByteArrayOutputStream();
		pic21.compress(Bitmap.CompressFormat.JPEG, 100, stream21);
		pic21.recycle();
		byte[] byteArray21 = stream21.toByteArray();
		try {
			stream21.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eccentricPushup.setImage(byteArray21);
		eccentricPushup.setId(exerciseDAO.addExercise(eccentricPushup));

		//1.5 PUSHUP
		Exercise pushup15 = new Exercise();
		pushup15.setName("1.5 push-up");
		pushup15.setDescription("Feet on the floor, legs straight, hands on the floor to the shoulders " +
				"level, arms straight. Lower your chest to the floor, push up halfway, then descend and push up again.");
		pushup15.setType(ExerciseType.chest);
		Bitmap pic22 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pushup15, options);
		ByteArrayOutputStream stream22 = new ByteArrayOutputStream();
		pic22.compress(Bitmap.CompressFormat.JPEG, 100, stream22);
		pic22.recycle();
		byte[] byteArray22 = stream22.toByteArray();
		try {
			stream22.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pushup15.setImage(byteArray22);
		pushup15.setId(exerciseDAO.addExercise(pushup15));


		/* back Exercises*/

		//PULL-UP
		Exercise pullup = new Exercise();
		pullup.setName("Pull-up");
		pullup.setDescription("Grab a bar, pull up until your chest hits the bar. Lower and repeat.");
		pullup.setType(ExerciseType.back);
		Bitmap pic23 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pullup, options);
		ByteArrayOutputStream stream23 = new ByteArrayOutputStream();
		pic23.compress(Bitmap.CompressFormat.JPEG, 100, stream23);
		pic23.recycle();
		byte[] byteArray23 = stream23.toByteArray();
		try {
			stream23.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pullup.setImage(byteArray23);
		pullup.setId(exerciseDAO.addExercise(pullup));

		//PIKE PUSHUP
		Exercise pikePushup = new Exercise();
		pikePushup.setName("Pike push-up");
		pikePushup.setDescription("Put your feet on a step, bench or any elevated " +
				"position. Legs straight, butt up in the air, upper body mostly straight. Complete a push-up.");
		pikePushup.setType(ExerciseType.back);
		Bitmap pic24 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pikepushup, options);
		ByteArrayOutputStream stream24 = new ByteArrayOutputStream();
		pic24.compress(Bitmap.CompressFormat.JPEG, 100, stream24);
		pic24.recycle();
		byte[] byteArray24 = stream24.toByteArray();
		try {
			stream24.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pikePushup.setImage(byteArray24);
		pikePushup.setId(exerciseDAO.addExercise(pikePushup));

		//HANDSTAND PUSHUP
		Exercise handstandPushup = new Exercise();
		handstandPushup.setName("Handstand push-up");
		handstandPushup.setDescription("For back and shoulders. Start with a handstand position " +
				"against a wall. Lower until your head almost touches the floor. Raise and repeat.");
		handstandPushup.setType(ExerciseType.back);
		Bitmap pic25 = BitmapFactory.decodeResource(context.getResources(), R.drawable.handstandpushup, options);
		ByteArrayOutputStream stream25 = new ByteArrayOutputStream();
		pic25.compress(Bitmap.CompressFormat.JPEG, 100, stream25);
		pic25.recycle();
		byte[] byteArray25 = stream25.toByteArray();
		try {
			stream25.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handstandPushup.setImage(byteArray25);
		handstandPushup.setId(exerciseDAO.addExercise(handstandPushup));

		//SIDE2SIDE PULLUP
		Exercise side2sidePullup = new Exercise();
		side2sidePullup.setName("Side-to-side pull-up");
		side2sidePullup.setDescription("Start with a normal pull-up (palms away from you), " +
				"but pull your body to one side to add emphasis to that side. Switch sides for each rep.");
		side2sidePullup.setType(ExerciseType.back);
		Bitmap pic26 = BitmapFactory.decodeResource(context.getResources(), R.drawable.side2sidepullup, options);
		ByteArrayOutputStream stream26 = new ByteArrayOutputStream();
		pic26.compress(Bitmap.CompressFormat.JPEG, 100, stream26);
		pic26.recycle();
		byte[] byteArray26 = stream26.toByteArray();
		try {
			stream26.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		side2sidePullup.setImage(byteArray26);
		side2sidePullup.setId(exerciseDAO.addExercise(side2sidePullup));

		//SUPERMAN
		Exercise superman = new Exercise();
		superman.setName("Superman");
		superman.setDescription("Lie face down with arms and legs extended. Raise the arms and legs, " +
				"your body should form a small curve. Hold this position, condiser 1 rep as 20 seconds.");
		superman.setType(ExerciseType.back);
		Bitmap pic27 = BitmapFactory.decodeResource(context.getResources(), R.drawable.superman, options);
		ByteArrayOutputStream stream27 = new ByteArrayOutputStream();
		pic27.compress(Bitmap.CompressFormat.JPEG, 100, stream27);
		pic27.recycle();
		byte[] byteArray27 = stream27.toByteArray();
		try {
			stream27.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		superman.setImage(byteArray27);
		superman.setId(exerciseDAO.addExercise(superman));


		/* abs Exercises*/

		//PLANK
		Exercise plank = new Exercise();
		plank.setName("Plank");
		plank.setDescription("Get into push-up position. Put your forearms on the floor, your elbows should make a 90 degrees angle. " +
				"Elbows should be beneath your shoulders. Hold this position, consider 1 rep as 1 minute standing.");
		plank.setType(ExerciseType.abs);
		Bitmap pic28 = BitmapFactory.decodeResource(context.getResources(), R.drawable.plank2, options);
		ByteArrayOutputStream stream28 = new ByteArrayOutputStream();
		pic28.compress(Bitmap.CompressFormat.JPEG, 100, stream28);
		pic28.recycle();
		byte[] byteArray28 = stream28.toByteArray();
		try {
			stream28.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plank.setImage(byteArray28);
		plank.setId(exerciseDAO.addExercise(plank));

		//SIDE PLANK
		Exercise sidePlank = new Exercise();
		sidePlank.setName("Side plank");
		sidePlank.setDescription("Lie on your side with right forearm resting on the ground, your feet on top of one another. Lift your body off the ground to straighten it, " +
				"supported by your forearm and feet. Hold the position, consider 1 rep as 30 seconds for each side.");
		sidePlank.setType(ExerciseType.abs);
		Bitmap pic29 = BitmapFactory.decodeResource(context.getResources(), R.drawable.sideplank3, options);
		ByteArrayOutputStream stream29 = new ByteArrayOutputStream();
		pic29.compress(Bitmap.CompressFormat.JPEG, 100, stream29);
		pic29.recycle();
		byte[] byteArray29 = stream29.toByteArray();
		try {
			stream29.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sidePlank.setImage(byteArray29);
		sidePlank.setId(exerciseDAO.addExercise(sidePlank));

		//V-UP
		Exercise vUp = new Exercise();
		vUp.setName("V-up");
		vUp.setDescription("Lie on the floor on your back. Hold your arms " +
				"straight above your head. Simultaneously lift your torso and legs, as if you were trying to reach your feet.");
		vUp.setType(ExerciseType.abs);
		Bitmap pic30 = BitmapFactory.decodeResource(context.getResources(), R.drawable.vup, options);
		ByteArrayOutputStream stream30 = new ByteArrayOutputStream();
		pic30.compress(Bitmap.CompressFormat.JPEG, 100, stream30);
		pic30.recycle();
		byte[] byteArray30 = stream30.toByteArray();
		try {
			stream30.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vUp.setImage(byteArray30);
		vUp.setId(exerciseDAO.addExercise(vUp));

		//CRUNCH
		Exercise crunch = new Exercise();
		crunch.setName("Crunch");
		crunch.setDescription("Lie on the floor on your back with knees bent. Lift both shoulders off the floor to move your " +
				"chest forward, then return to starting position. Your hands can be behind the neck or crossed over your chest.");
		crunch.setType(ExerciseType.abs);
		Bitmap pic31 = BitmapFactory.decodeResource(context.getResources(), R.drawable.crunch, options);
		ByteArrayOutputStream stream31 = new ByteArrayOutputStream();
		pic31.compress(Bitmap.CompressFormat.JPEG, 100, stream31);
		pic31.recycle();
		byte[] byteArray31 = stream31.toByteArray();
		try {
			stream31.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		crunch.setImage(byteArray31);
		crunch.setId(exerciseDAO.addExercise(crunch));

		//CROSS CRUNCH
		Exercise crossCrunch = new Exercise();
		crossCrunch.setName("Cross crunch");
		crossCrunch.setDescription("Lie on the floor on your back with knees bent. Lift your right shoulder off the floor toward " +
				"your left knee, while raising the left knee (but keeping the leg bent). Return to starting position and swap.");
		crossCrunch.setType(ExerciseType.abs);
		Bitmap pic32 = BitmapFactory.decodeResource(context.getResources(), R.drawable.sidecrunch, options);
		ByteArrayOutputStream stream32 = new ByteArrayOutputStream();
		pic32.compress(Bitmap.CompressFormat.JPEG, 100, stream32);
		pic32.recycle();
		byte[] byteArray32 = stream32.toByteArray();
		try {
			stream32.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		crossCrunch.setImage(byteArray32);
		crossCrunch.setId(exerciseDAO.addExercise(crossCrunch));

		//REVERSE CRUNCH
		Exercise reverseCrunch = new Exercise();
		reverseCrunch.setName("Reverse crunch");
		reverseCrunch.setDescription("Lie on the floor on your back, legs up so that your thighs are perpendicular to the floor, arms to the side of your torso. " +
				"Move your knees towards your chest while you raise your hips off the floor. Return to starting position.");
		reverseCrunch.setType(ExerciseType.abs);
		Bitmap pic33 = BitmapFactory.decodeResource(context.getResources(), R.drawable.reversecrunch2, options);
		ByteArrayOutputStream stream33 = new ByteArrayOutputStream();
		pic33.compress(Bitmap.CompressFormat.JPEG, 100, stream33);
		pic33.recycle();
		byte[] byteArray33 = stream33.toByteArray();
		try {
			stream33.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reverseCrunch.setImage(byteArray33);
		reverseCrunch.setId(exerciseDAO.addExercise(reverseCrunch));

		//RAISED LEG CIRCLES
		Exercise raisedLegCircles = new Exercise();
		raisedLegCircles.setName("Raised leg circles");
		raisedLegCircles.setDescription("Lie on the floor on your back, raise " +
				"your legs to 60 degrees and draw circles in the air with your toes. 1 circle is 1 rep. Swap circular motion in the middle of the set.");
		raisedLegCircles.setType(ExerciseType.abs);
		Bitmap pic34 = BitmapFactory.decodeResource(context.getResources(), R.drawable.circle3, options);
		ByteArrayOutputStream stream34 = new ByteArrayOutputStream();
		pic34.compress(Bitmap.CompressFormat.JPEG, 100, stream34);
		pic34.recycle();
		byte[] byteArray34 = stream34.toByteArray();
		try {
			stream34.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		raisedLegCircles.setImage(byteArray34);
		raisedLegCircles.setId(exerciseDAO.addExercise(raisedLegCircles));


		/************************************* WORKOUTS *****************************************/

		//Leg day 1
		CrossfitWorkout workout1 = new CrossfitWorkout();
		workout1.setName("Leg day 1");
		workout1.setDescription("This workout is intended to build your legs. Legs are often forgotten in workouts, while they have the " +
				"most powerful muscles in the body. If you can still walk after this workout, just raise the number of sets.");
		workout1.setSets(4);
		squat.setReps(40);
		workout1.addExercise(squat);
		calfRaise.setReps(50);
		workout1.addExercise(calfRaise);
		wallSit.setReps(1);
		workout1.addExercise(wallSit);
		burpees.setReps(35);
		workout1.addExercise(burpees);
		mountainClimber.setReps(50);
		workout1.addExercise(mountainClimber);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout1);


		//Leg day 2
		CrossfitWorkout workout2 = new CrossfitWorkout();
		workout2.setName("Leg day 2");
		workout2.setDescription("Here is another version of the leg day workout. Performing various " +
				"exercises is good to better build your muscles.");
		workout2.setSets(4);
		pistolSquat.setReps(20);
		workout2.addExercise(pistolSquat);
		jumpingLunge.setReps(30);
		workout2.addExercise(jumpingLunge);
		workout2.addExercise(wallSit);
		tuckJump.setReps(20);
		workout2.addExercise(tuckJump);
		burpees.setReps(30);
		workout2.addExercise(burpees);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout2);


		//Abs of steel
		CrossfitWorkout workout3 = new CrossfitWorkout();
		workout3.setName("Abs of steel");
		workout3.setDescription("Want to get abs of steel before summer? Let's get to work then. " +
				"Don't hesitate to raise the number of reps and sets if it becomes too easy.");
		workout3.setSets(4);
		crunch.setReps(50);
		workout3.addExercise(crunch);
		reverseCrunch.setReps(50);
		workout3.addExercise(reverseCrunch);
		plank.setReps(1);
		workout3.addExercise(plank);
		crossCrunch.setReps(40);
		workout3.addExercise(crossCrunch);
		sidePlank.setReps(1);
		workout3.addExercise(sidePlank);
		raisedLegCircles.setReps(50);
		workout3.addExercise(raisedLegCircles);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout3);


		//Core workout
		CrossfitWorkout workout4 = new CrossfitWorkout();
		workout4.setName("Core workout");
		workout4.setDescription("A balanced workout to train the whole body.");
		workout4.setSets(4);
		burpees.setReps(30);
		workout4.addExercise(burpees);
		pushup.setReps(25);
		workout4.addExercise(pushup);
		crunch.setReps(40);
		workout4.addExercise(crunch);
		squat.setReps(40);
		workout4.addExercise(squat);
		plank.setReps(1);
		workout4.addExercise(plank);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout4);


		//Pyramid
		CrossfitWorkout workout5 = new CrossfitWorkout();
		workout5.setName("Pyramid");
		workout5.setDescription("Pyramidal workout is a well-known way of " +
				"training for its efficiency. This workout will also train the whole body.");
		workout5.setSets(4);
		crunch.setReps(45);
		workout5.addExercise(crunch);
		burpees.setReps(35);
		workout5.addExercise(burpees);
		pushup.setReps(25);
		workout5.addExercise(pushup);
		dips.setReps(15);
		workout5.addExercise(dips);
		pullup.setReps(10);
		workout5.addExercise(pullup);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout5);


		//Strength upper body 1
		CrossfitWorkout workout6 = new CrossfitWorkout();
		workout6.setName("Strength upper body 1");
		workout6.setDescription("This workout helps you get strength in your arms, back and chest. " +
				"To have better effect, perform each rep slowly, to make sure your muscles burn.");
		workout6.setSets(3);
		pushup15.setReps(20);
		workout6.addExercise(pushup15);
		side2sidePullup.setReps(10);
		workout6.addExercise(side2sidePullup);
		dips.setReps(20);
		workout6.addExercise(dips);
		handstandPushup.setReps(10);
		workout6.addExercise(handstandPushup);
		clappingPushup.setReps(20);
		workout6.addExercise(clappingPushup);
		armCircles.setReps(200);
		workout6.addExercise(armCircles);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout6);


		//Strength upper body 2
		CrossfitWorkout workout7 = new CrossfitWorkout();
		workout7.setName("Strength upper body 2");
		workout7.setDescription("Like the last one, this workout targets strength in the upper body, " +
				"with difficult exercises and lower number of reps. Perform each rep slowly, to make sure your muscles burn.");
		workout7.setSets(3);
		oneArmPushup.setReps(8);
		workout7.addExercise(oneArmPushup);
		feetElevatedPushup.setReps(25);
		workout7.addExercise(feetElevatedPushup);
		pullup.setReps(10);
		workout7.addExercise(pullup);
		tricepsExtensions.setReps(20);
		workout7.addExercise(tricepsExtensions);
		pikePushup.setReps(20);
		workout7.addExercise(pikePushup);
		eccentricPushup.setReps(15);
		workout7.addExercise(eccentricPushup);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout7);

		
		//Cardio workout
		CrossfitWorkout workout8 = new CrossfitWorkout();
		workout8.setName("Cardio workout");
		workout8.setDescription("This workout helps you training your heart and breath. Beginners should " +
				"take some break between exercises, while more experienced ones will try to break the timer.");
		workout8.setSets(3);
		burpees.setReps(35);
		workout8.addExercise(burpees);
		boxer.setReps(150);
		workout8.addExercise(boxer);
		jumpingLunge.setReps(35);
		workout8.addExercise(jumpingLunge);
		vUp.setReps(30);
		workout8.addExercise(vUp);
		tuckJump.setReps(25);
		workout8.addExercise(tuckJump);
		wideGripPushup.setReps(30);
		workout8.addExercise(wideGripPushup);
		raisedLegCircles.setReps(50);
		workout8.addExercise(raisedLegCircles);
		
		crossfitWorkoutDAO.addCrossfitWorkout(workout8);

	}
}
