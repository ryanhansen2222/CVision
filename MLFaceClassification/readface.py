import pickle
import numpy as np
import cv2 as cv

face_cascade = cv.CascadeClassifier(cv.data.haarcascades + 'haarcascade_frontalface_default.xml')

recognizer = cv.face.LBPHFaceRecognizer_create()
recognizer.read("trainer.yml")
labels = {}
with open("labels.pickle", 'rb') as f:
	of_labels = pickle.load(f)
	labels = {v:k for k, v in of_labels.items()}

cv.namedWindow("Image")

img = cv.imread("finale.jpg", cv.IMREAD_COLOR)
gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

faces = face_cascade.detectMultiScale(gray, 1.1, 5)
print("faces")

#cap = cv.VideoCapture(0)

for (x,y,w,h) in faces:
	cv.rectangle(img, (x,y), (x+w, y+h), (255,0,0), 2)
	roi_gray = gray[y:y+h, x:x+w]
	roi_color = img[y:y+h, x:x+w]
	idy, conf = recognizer.predict(roi_gray)

	name = labels[idy]
	print('Heres what we got: ', name, conf)

	if conf >= 45 and conf <= 95:
		name = labels[idy]
		print(name)
		#Find documentation
		cv.putText(img, labels[idy], (x,y), cv.FONT_HERSHEY_SIMPLEX, 1, (0,0,0), 2, cv.LINE_AA)

cv.imshow('Image', img)
cv.waitKey(0)
cv.destroyAllWindows()
#cap.release()

