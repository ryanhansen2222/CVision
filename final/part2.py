import pickle
import numpy as np
import cv2 as cv

face_cascade = cv.CascadeClassifier(cv.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_cascade = cv.CascadeClassifier(cv.data.haarcascades + 'haarcascade_eye.xml')

'''
recognizer = cv.face.LBPHFaceRecognizer_create()
recognizer.read("trainer.yml")
labels = {}
with open("labels.pickle", 'rb') as f:
	of_labels = pickle.load(f)
	labels = {v:k for k, v in of_labels.items()}
'''


img = cv.imread("hunter.jpg", cv.IMREAD_COLOR)
gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

cv.namedWindow("Ryan Hansen")

faces = face_cascade.detectMultiScale(gray, 1.1, 5)
eyes = eye_cascade.detectMultiScale(gray, 1.1, 5)
print("faces")

#cap = cv.VideoCapture(0)

for (x,y,w,h) in faces:
	cv.rectangle(img, (x,y), (x+w, y+h), (255,0,0), 2)
	roi_gray = gray[y:y+h, x:x+w]
	roi_color = img[y:y+h, x:x+w]
	#idy, conf = recognizer.predict(roi_gray)


	for (xe,ye,we,he) in eyes:
		xe = xe-10
		ye = ye-10
		we = we+20
		he = he+20
		if (xe > x and ye > y and xe+we < w+x and ye+he < y+h):
			print(xe, xe+we)
			#cv.rectangle(img, (xe,ye), (xe+we, ye+he), (0,255,0), 2)
			roi_gray = gray[ye:ye+he, xe:xe+we]
			roi_color = img[ye:ye+he, xe:xe+we]


			for yp in range(ye, ye+he):
				for xp in range(xe, xe+we):
				#print(pixel)
					gray[yp,xp] = gray[yp,xp]-20
					img[yp, xp] = img[yp,xp] + np.matrix([130,0,0])
			

	#name = labels[idy]
	name = 'hunter'
	#print('Heres what we got: ', name, conf)

	print(name)
	cv.putText(img, name, (x,y), cv.FONT_HERSHEY_SIMPLEX, 1, (0,0,0), 2, cv.LINE_AA)
	text = 'When you mess with the best...'
	cv.putText(img, text, (70,500), cv.FONT_HERSHEY_SIMPLEX, 1, (0,0,0), 2, cv.LINE_AA)




cv.imshow('Ryan Hansen', img)
cv.imshow('Ryan', gray)
cv.waitKey(0)
cv.destroyAllWindows()
#cap.release()


