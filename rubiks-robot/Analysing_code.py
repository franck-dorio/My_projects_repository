"""Code d'analyse des faces"""

import cv2
import matplotlib.pyplot as plt

img= cv2.imread("/Users/franckdorio/Documents/programme tipe/facecube3.jpg",1)

print(img)

def bgrtorgb(img):
    img1=[]
    for i in range(len(img)):
        img1.append([])
    for j in range (len(img)):
        for i in range(len(img[1])):
            img1[j].append([])
    for i in range (len(img1)):
        for j in range (len(img1[1])):
            img1[i][j]=[img[i][j][2],img[i][j][1],img[i][j][0]]
    return img1
    
img2=bgrtorgb(img)

"""
   
for i in range (100):
    for j in range (100):
        img2[170+i][270+j]=[27, 255, 0  ]

for i in range (100):
     for j in range (100):
         img2[170+i][425+j]=[27, 255, 0  ]       

for i in range (100):
    for j in range (100):
        img2[170+i][575+j]=[27, 255, 0  ] 

for i in range (100):
    for j in range (100):
        img2[320+i][270+j]=[27, 255, 0  ]

for i in range (100):
     for j in range (100):
         img2[320+i][425+j]=[27, 255, 0  ]       

for i in range (100):
    for j in range (100):
        img2[320+i][575+j]=[27, 255, 0  ]  
        
for i in range (100):
    for j in range (100):
        img2[470+i][270+j]=[27, 255, 0  ]

for i in range (100):
     for j in range (100):
         img2[470+i][425+j]=[27, 255, 0  ]       

for i in range (100):
    for j in range (100):
        img2[470+i][575+j]=[27, 255, 0  ]  
        
"""
def distancepixel(pixel1,pixel2):
    N=0
    for i in range(3):
        N=N+abs(pixel1[i]-pixel2[i])
    return N

colorref=[
    ["orange",[255,128,0]],
    ['red',[255,0,0]],
    ["blue",[0,0,255]],
    ["green",[0,255,0]],
    ["yellow",[255,235,0]],
    ["grey",[255,255,255]],
    ]

def reconnaissancecouleur(pixel):
    N=1000
    couleur=""
    for i in colorref:
        if distancepixel(i[1],pixel)<=N:
            N=distancepixel(i[1],pixel)
            couleur=i[0]
    return couleur

def cadrepixel(position,img):
    img1=img
    for j in range(7):
        for i in range(50+2*j+1):
            img1[position[0]-25-j+i][position[1]-25-j]=[0,255,0]
            img1[position[0]-25-j+i][position[1]+25+j]=[0,255,0]
            img1[position[0]-25-j][position[1]-25-j+i]=[0,255,0]
            img1[position[0]+25+j][position[1]-25-j+i]=[0,255,0]
    
    return (img1)

def cadrepixelpetit(position,img):
    img1=img
    for j in range(3):
        for i in range(20+2*j+1):
            img1[position[0]-10-j+i][position[1]-10-j]=[0,255,0]
            img1[position[0]-10-j+i][position[1]+10+j]=[0,255,0]
            img1[position[0]-10-j][position[1]-10-j+i]=[0,255,0]
            img1[position[0]+10+j][position[1]-10-j+i]=[0,255,0]
    
    return (img1)

def testcouleur(position, img):
    img2=img
    img2=cadrepixel(position,img2)          
    print(reconnaissancecouleur(img2[position[0]][position[1]]))   
    print(img2[position[0]][position[1]])  
    
    return img2
def testcouleurpeti(position, img):
    img2=img
    img2=cadrepixelpetit(position,img2)          
    print(reconnaissancecouleur(img2[position[0]][position[1]]))   
    print(img2[position[0]][position[1]])  
    
    return img2
    
img2=testcouleur([400,300],img2)        
plt.imshow(img2)
plt.show()

