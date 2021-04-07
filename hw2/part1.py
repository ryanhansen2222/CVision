import numpy as np
import cv2

def to_grey(frame,hsv):
    # define range of blue color in HSV
    lower = np.array([minH,minS,minV])
    upper = np.array([maxH,maxS,maxV])
    print(lower, upper)
    # Threshold the HSV image to get only blue colors
    mask = cv2.inRange(hsv, lower, upper)
    # Bitwise-AND mask and original image
    res = cv2.bitwise_and(frame,frame, mask= mask)
    return mask


def to_hsv(frame):
    # Convert BGR to HSV
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    return hsv

def post_events(event, x,y,flags,param):
    if(event == cv2.EVENT_LBUTTONDOWN):
        print('X: ', x, 'Y: ', y, 'Flags: ', flags, 'Param: ',param, 'HSV: ', frame[x][y])


def setminH(val):
    minH = val
    
def setmaxH(val):
    maxH = val

def setminS(val):
    minS = val

def setmaxS(val):
    maxS = val

def setminV(val):
    minV = val

def setmaxV(val):
    maxV = val
    print(maxV)

#INIT STUFF
cap = cv2.VideoCapture(0)

init = True
ret, frame = cap.read()
minH = 0
minS =0
minV=0
maxH=255
maxS=255
maxV=255


#RUN VIDEO CAPTURE LOOP
while(True):

    # Capture frame-by-frame
    ret, frame = cap.read()

    # Our operations on the frame come here
    #gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Display the resulting frame
    modified = to_hsv(frame)
    modified = to_grey(frame, modified)
    cv2.imshow('frame',frame)
    cv2.imshow('modified', modified)

    

    if(init):
        init == False
        cv2.setMouseCallback('modified',post_events)

        minHbar = 'minH'
        cv2.createTrackbar('minH', 'modified' , 0, 255, setminH)

        minSbar = 'minS'
        cv2.createTrackbar('minS', 'modified' , 0, 255, setminS)

        minVbar = 'minV'
        cv2.createTrackbar('minV', 'modified' , 0, 255, setminV)

        maxHbar = 'maxH'
        cv2.createTrackbar('maxH', 'modified' , 0, 255, setmaxH)

        maxSbar = 'maxS'
        cv2.createTrackbar('maxS', 'modified' , 0, 255, setmaxS)

        maxVbar = 'maxV'
        cv2.createTrackbar('maxV', 'modified' , 0, 255, setmaxV)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# When everything done, release the capture
cap.release()
cv2.destroyAllWindows()
