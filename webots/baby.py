from controller import *
import cv2
import numpy as np



def to_hsv(frame):
    # Convert BGR to HSV
    hsv = cv2.cvtColor(np.array(frame), cv2.COLOR_BGR2HSV)
    return hsv

def find_orange(frame):
	ORANGE_MIN = np.matrix([11, 20, 50])
	ORANGE_MAX = np.matrix([50, 255, 255])
	newframe = cv2.inRange(frame, ORANGE_MIN, ORANGE_MAX)
	return newframe

def count_orange(frame):
	height, width = np.shape(frame)

	total = height*width

	targety = int(height/2)
	targetx = int(width/2)

	numwhite = 0
	xcm = 0
	ycm = 0
	for x in range(width):
		for y in range(height):
			if(frame[y][x] > 150):
				xcm = xcm + x
				ycm = ycm + y
				numwhite += 1

	xcm = int(xcm/numwhite)
	ycm = int(ycm/numwhite)

	proportion = numwhite/total
	print(proportion, xcm, ycm)


	if(proportion < .3):
		return 'forward'
	elif(proportion > .7):
		return 'backward'
	elif((xcm-targetx) > 5):
		return 'right'
	elif(targetx-xcm > 5):
		return 'left'
	else:
		return 'stop'




robot = Robot()
print('hello')

timestep = int(robot.getBasicTimeStep())


leftMotor = robot.getDevice('left wheel motor')
rightMotor = robot.getDevice('right wheel motor')
leftMotor.setPosition(float('inf'))
rightMotor.setPosition(float('inf'))

cam = robot.getDevice("camera")
cam.enable(timestep)

cv2.namedWindow("Video")
cv2.namedWindow("Canny")
cv2.namedWindow("ORANGE")
once = True

while robot.step(timestep) != 9000:

	#First thing we get image
	img = cam.getImage()
	width = cam.getWidth()
	height = cam.getHeight()
	temppic = np.frombuffer(img, np.uint8)
	out = np.reshape(temppic, (height, width, 4))
	can = cv2.Canny(out, 40, 80)

	#Analyze image

	hsv = cv2.cvtColor(out, cv2.COLOR_BGR2HSV)
	orange = find_orange(hsv)
	decision = count_orange(orange)


	#Map orange to white in greyscale

	#Based on num white pixels, and center of those white pixels


	#Move based on decision
	if(decision == 'forward'):
		leftMotor.setVelocity(4.0)
		rightMotor.setVelocity(4.0)
	elif decision =='backward':
		leftMotor.setVelocity(-4.0)
		rightMotor.setVelocity(-4.0)
	elif decision == 'right':
		leftMotor.setVelocity(4.0)
		rightMotor.setVelocity(2.6)
	elif decision == 'left':
		leftMotor.setVelocity(2.6)
		rightMotor.setVelocity(4.0)
	else:
		leftMotor.setVelocity(0.0)
		rightMotor.setVelocity(0.0)
		



	cv2.imshow("ORANGE", orange)
	cv2.imshow("Video", out)
	cv2.imshow("Canny", can)
	k = cv2.waitKey(1)
	if k==27:
		break
	

cv2.destroyAllWindows()
