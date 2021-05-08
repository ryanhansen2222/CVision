from controller import *
import cv2
import numpy as np



def to_hsv(frame):
    # Convert BGR to HSV
    hsv = cv2.cvtColor(np.array(frame), cv2.COLOR_BGR2HSV)
    return hsv

def find_black(frame):
	BLACK_MIN = np.matrix([0, 0, 0])
	BLACK_MAX = np.matrix([255, 255, 70])
	newframe = cv2.inRange(frame, BLACK_MIN, BLACK_MAX)
	return newframe

def count_black(frame):
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

	xcm = int(xcm/(numwhite+1))
	ycm = int(ycm/(numwhite+1))

	proportion = numwhite/total
	print(proportion, xcm, ycm)


	if(proportion < .001):
		return 'stop'
	elif((xcm-targetx) > 5):
		return 'right'
	elif(targetx-xcm > 5):
		return 'left'
	else:
		return 'forward'




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
cv2.namedWindow("Line")
once = True

recent = None
count = 0
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
	black = find_black(hsv)
	decision = count_black(black)


	#Map orange to white in greyscale

	#Based on num white pixels, and center of those white pixels


	#Move based on decision

		
	if(decision == 'forward'):
		leftMotor.setVelocity(6.0)
		rightMotor.setVelocity(6.0)
	elif decision =='backward':
		leftMotor.setVelocity(-4.0)
		rightMotor.setVelocity(-4.0)
	elif decision == 'right':
		leftMotor.setVelocity(6.0)
		rightMotor.setVelocity(2.0)
		recent = 'right'
	elif decision == 'left':
		leftMotor.setVelocity(2.0)
		rightMotor.setVelocity(6.0)
		recent = 'left'
	elif recent == 'left':
		count +=1
		leftMotor.setVelocity(1.0)
		rightMotor.setVelocity(6.0)
	elif recent == 'right':
		count +=1
		leftMotor.setVelocity(1.0)
		rightMotor.setVelocity(6.0)
	else:

		leftMotor.setVelocity(0.0)
		rightMotor.setVelocity(0.0)
		


	if(count > 5):
		recent = None
		if(count < 50):
			count += 1

			leftMotor.setVelocity(3.0)
			rightMotor.setVelocity(3.0)
		
			

	cv2.imshow("Line", black)
	cv2.imshow("Video", out)
	cv2.imshow("Canny", can)
	k = cv2.waitKey(1)
	if k==27:
		break
	

cv2.destroyAllWindows()
