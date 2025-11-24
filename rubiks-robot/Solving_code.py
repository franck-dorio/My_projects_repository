'''code fonction final'''

import matplotlib
import matplotlib.pyplot as plt
import random

"""
matrice d'un cube:

M=[
   [U1,U2,U3,U4,U5,U6,U7,U8,U9]
   [L1,L2,L3,L4,L5,L6,L7,L8,L9]
   [F1,F2,F3,F4,F5,F6,F7,F8,F9]
   [R1,R2,R3,R4,R5,R6,R7,R8,R9]
   [B1,B2,B3,B4,B5,B6,B7,B8,B9]
   [D1,D2,D3,D4,D5,D6,D7,D8,D9]
   ]


"""

N=[
   ['grey','grey','grey','grey','grey','grey','grey','grey','grey'],
   ['red','red','red','red','red','red','red','red','red'],
   ['blue','blue','blue','blue','blue','blue','blue','blue','blue'],
   ['orange','orange','orange','orange','orange','orange','orange','orange','orange'],
   ['green','green','green','green','green','green','green','green','green'],
   ['yellow','yellow','yellow','yellow','yellow','yellow','yellow','yellow','yellow']
   ]


fig = plt.figure()
ax = fig.add_subplot(111)



plt.xlim([-400, 400])
plt.ylim([-400, 400])

    

    
    
def printcube (M):
    
    print(M)
    D=[[0 for i in range (9)] for _ in range (6)]
    
    for j in range (9):
            D[0][j]=matplotlib.patches.Rectangle((-150+(j%3)*50 ,315-(j//3)*50), 50, 50, color=M[0][j])
    for j in range (9):
         	D[1][j]=matplotlib.patches.Rectangle((-315+(j%3)*50 ,150-(j//3)*50), 50, 50, color=M[1][j])
    for j in range (9):
         	D[2][j]=matplotlib.patches.Rectangle((-150+(j%3)*50 ,150-(j//3)*50), 50, 50, color=M[2][j])
    for j in range (9):
        	D[3][j]=matplotlib.patches.Rectangle((15+(j%3)*50 ,150-(j//3)*50), 50, 50, color=M[3][j])    
    for j in range (9):
        	D[4][j]=matplotlib.patches.Rectangle((180+(j%3)*50 ,150-(j//3)*50), 50, 50, color=M[4][j])
    for j in range (9):
        	D[5][j]=matplotlib.patches.Rectangle((-150+(j%3)*50 ,-15-(j//3)*50), 50, 50, color=M[5][j])
    for i in range (6):
            for j in range (9):
                    ax.add_patch(D[i][j])
                    
printcube(N)
    

def moveR (M):
    R=[]
    for j in range(6):
        L=[]
        for i in range (9):
            L.append(M[j][i])
        R.append(L)
    for i in range (1,4):
    	R[0][i*3-1]=M[2][i*3-1]
    	R[2][i*3-1]=M[5][i*3-1]
    R[4][0]=M[0][8]
    R[4][3]=M[0][5]
    R[4][6]=M[0][2]
    R[5][2]=M[4][6]
    R[5][5]=M[4][3]
    R[5][8]=M[4][0]
    R[3][0]=M[3][6]
    R[3][1]=M[3][3]
    R[3][2]=M[3][0]
    R[3][3]=M[3][7]
    R[3][5]=M[3][1]
    R[3][6]=M[3][8]
    R[3][7]=M[3][5]
    R[3][8]=M[3][2]
    return R

def moveR_1 (M):
    return moveR(moveR(moveR(M)))

def moveL (M):
    ML=[]
    for j in range(6):
        L=[]
        for i in range (9):
            L.append(M[j][i])
        ML.append(L)
    ML[0][0]=M[2][0]
    ML[0][3]=M[2][3]
    ML[0][6]=M[2][6]
    ML[2][0]=M[5][0]
    ML[2][3]=M[5][3]
    ML[2][6]=M[5][6]
    ML[4][2]=M[0][6]
    ML[4][5]=M[0][3]
    ML[4][8]=M[0][0]
    ML[5][0]=M[4][8]
    ML[5][3]=M[4][5]
    ML[5][6]=M[4][2]
    ML[1][0]=M[1][2]
    ML[1][1]=M[1][5]
    ML[1][2]=M[1][8]
    ML[1][3]=M[1][1]
    ML[1][5]=M[1][7]
    ML[1][6]=M[1][0]
    ML[1][7]=M[1][3]
    ML[1][8]=M[1][6]
    return ML

def moveL_1 (M):
    return moveL(moveL(moveL(M)))

def moveU (M):
    U=[]
    for j in range(6):
        L=[]
        for i in range (9):
            L.append(M[j][i])
        U.append(L)
    U[0][0]=M[0][6]
    U[0][1]=M[0][3]
    U[0][2]=M[0][0]
    U[0][3]=M[0][7]
    U[0][5]=M[0][1]
    U[0][6]=M[0][8]
    U[0][7]=M[0][5]
    U[0][8]=M[0][2]
    for i in range (3):
    	for j in range (3):
        	U[i+1][j]=M[i+2][j]
    for i in range (3):
    	U[4][i]=M[1][i]
    return U

def moveU_1 (M):
    return moveU(moveU(moveU(M)))

def moveD (M):
    MD=[]
    for j in range(6):
        L=[]
        for i in range (9):
            L.append(M[j][i])
        MD.append(L)
    
    for i in range (3):
    	for j in range (3):
        	MD[i+2][j+6]=M[i+1][j+6]
    for i in range (3):
        MD[1][i+6]=M[4][i+6]
    MD[5][0]=M[5][6]
    MD[5][1]=M[5][3]
    MD[5][2]=M[5][0]
    MD[5][3]=M[5][7]
    MD[5][5]=M[5][1]
    MD[5][6]=M[5][8]
    MD[5][7]=M[5][5]
    MD[5][8]=M[5][2]
    return MD

def moveD_1 (M):
    return moveD(moveD(moveD(M)))

def moveF (M):
    MF=[]
    for j in range(6):
        L=[]
        for i in range (9):
            L.append(M[j][i])
        MF.append(L)
    MF[2][0]=M[2][6]
    MF[2][1]=M[2][3]
    MF[2][2]=M[2][0]
    MF[2][3]=M[2][7]
    MF[2][5]=M[2][1]
    MF[2][6]=M[2][8]
    MF[2][7]=M[2][5]
    MF[2][8]=M[2][2]
    MF[0][6]=M[1][8]
    MF[0][7]=M[1][5]
    MF[0][8]=M[1][2]
    MF[3][0]=M[0][6]
    MF[3][3]=M[0][7]
    MF[3][6]=M[0][8]
    MF[5][0]=M[3][6]
    MF[5][1]=M[3][3]
    MF[5][2]=M[3][0]
    MF[1][2]=M[5][0]
    MF[1][5]=M[5][1]
    MF[1][8]=M[5][2]
    return MF

def moveF_1 (M):
    return moveF(moveF(moveF(M)))

def moveB (M):
    MB=[]
    for j in range(6):
        L=[]
        for i in range (9):
            L.append(M[j][i])
        MB.append(L)
    MB[4][0]=M[4][6]
    MB[4][1]=M[4][3]
    MB[4][2]=M[4][0]
    MB[4][3]=M[4][7]
    MB[4][5]=M[4][1]
    MB[4][6]=M[4][8]
    MB[4][7]=M[4][5]
    MB[4][8]=M[4][2]
    MB[0][0]=M[3][2]
    MB[0][1]=M[3][5]
    MB[0][2]=M[3][8]
    MB[3][2]=M[5][8]
    MB[3][5]=M[5][7]
    MB[3][8]=M[5][6]
    MB[5][8]=M[1][6]
    MB[5][7]=M[1][3]
    MB[5][6]=M[1][0]
    MB[1][0]=M[0][2]
    MB[1][3]=M[0][1]
    MB[1][6]=M[0][0]
    return MB

def moveB_1 (M):
    return moveB(moveB(moveB(M)))
    

def retournecube(M):
    A=moveR(moveR(moveL(moveL(M))))
    A[0][1]=M[5][1]
    A[0][4]=M[5][4]
    A[0][7]=M[5][7]
    A[2][1]=M[4][7]
    A[2][4]=M[4][4]
    A[2][7]=M[4][1]
    A[5][1]=M[0][1]
    A[5][4]=M[0][4]
    A[5][7]=M[0][7]
    A[4][1]=M[2][7]
    A[4][4]=M[2][4]
    A[4][7]=M[2][1]
    return A

def y(M):
    A=moveU(moveD_1(M))
    A[1][3]=M[2][3]
    A[1][4]=M[2][4]
    A[1][5]=M[2][5]
    A[2][3]=M[3][3]
    A[2][4]=M[3][4]
    A[2][5]=M[3][5]
    A[3][3]=M[4][3]
    A[3][4]=M[4][4]
    A[3][5]=M[4][5]
    A[4][3]=M[1][3]
    A[4][4]=M[1][4]
    A[4][5]=M[1][5]
    return A
    


def arrete (M):
    L=[[] for i in range(6)]
    for i in range(6):
        for j in range (9):
            if j%2==1:
                L[i].append(M[i][j])
    return L
            

Listedescoup=['R','R_1','L','L_1','U','U_1','D','D_1','F','F_1','B','B_1','T','Y']

        
def makeamove (M,i): 
    if i ==0:
        return moveR(M)
    if i ==1:
        return moveR_1(M)
    if i ==2:
        return moveL(M)
    if i ==3:
        return moveL_1(M)
    if i ==4:
        return moveU(M)
    if i ==5:
        return moveU_1(M)
    if i ==6:
        return moveD(M)
    if i ==7:
        return moveD_1(M)
    if i ==8:
        return moveF(M)
    if i ==9:
        return moveF_1(M)
    if i ==10:
        return moveB(M)
    if i ==11:
        return moveB_1(M)
    if i==12:
        return retournecube(M)
    if i==13:
        return y(M)
    
def randomcube ():
    M=N[:]
    for i in range (25):
        j=random.randint(0,11)
        M=makeamove(M,j)
    return M

def printlistecoups(L):
    
    M=[]
    for i in L:
        if i ==0:
            M.append("R")
        if i ==1:
            M.append("R_1")
        if i ==2:
            M.append("L")
        if i ==3:
            M.append("L_1")
        if i ==4:
            M.append("U")
        if i ==5:
            M.append("U_1")
        if i ==6:
            M.append("D")
        if i ==7:
            M.append("D_1")
        if i ==8:
            M.append("F")
        if i ==9:
            M.append("F_1")
        if i ==10:
            M.append("B")
        if i ==11:
            M.append("B_1")
        if i==12:
            M.append("T")
        if i==13:
            M.append("Y")
    print( M)
    return M

def listecoupnombre(L):
    A=[]
    for i in L:
        for j in range(14):
            if i==Listedescoup[j]:
                A.append(j)
    return A
    
def applycoup(M,L):
    A=M
    for i in L:
        A=makeamove(A, i)
    return(A)
                          
def arreteatache(M,face,pos):
    L=arrete (M)
    if face==0:
        if pos==0:
            return [L[4][0],"green"]
        if pos==1:
            return [L[1][0],"red"]
        if pos==2:
            return [L[3][0],"orange"]
        if pos==3:
            return [L[2][0],"blue"]
    if face==1:
        if pos==0:
            return [L[0][1],"grey"]
        if pos==1:
            return [L[4][2],"green"]
        if pos==2:
            return [L[2][1],"blue"]
        if pos==3:
            return [L[5][1],"yellow"]
    if face==2:
        if pos==0:
            return [L[0][3],"grey"]
        if pos==1:
            return [L[1][2],"red"]
        if pos==2:
            return [L[3][1],"orange"]
        if pos==3:
            return [L[5][0],"yellow"]
    if face==3:
        if pos==0:
            return [L[0][2],"grey"]
        if pos==1:
            return [L[2][2],"blue"]
        if pos==2:
            return [L[4][1],"green"]
        if pos==3:
            return [L[5][2],"yellow"]
    if face==4:
        if pos==0:
            return [L[0][0],"grey"]
        if pos==1:
            return [L[3][2],"orange"]
        if pos==2:
            return [L[1][1],"red"]
        if pos==3:
            return [L[5][3],"yellow"]
    if face==5:
        if pos==0:
            return [L[2][3],"blue"]
        if pos==1:
            return [L[1][3],"red"]
        if pos==2:
            return [L[3][3],"orange"]
        if pos==3:
            return [L[4][3],"green"]

def normecroix2(M):
    N=0
    A=arrete(M)
    for i in range (6):
        for j in range(4):
            if A[i][j]== "grey":
                a=arreteatache(M, i, j)[0]
                b=arreteatache(M, i, j)[1]
                if a!=b:
                    N=N+1
                if i==5:
                    N=N+2
                if i>=1 and i<=4:
                    if j==0 or j==3:
                        N=N+2
                    else:
                       N=N+1

    return N

def normecroix3(M):
    N=0
    A=arrete(M)
    for i in range (6):
        for j in range(4):
            if A[i][j]== "grey":
                a=arreteatache(M, i, j)[0]
                b=arreteatache(M, i, j)[1]
                if a!=b:
                    N=N+1
                if i==5:
                    N=N+2
                if i>=1 and i<=4:
                    if j==0 or j==3:
                        N=N+2
                    else:
                       N=N+1
    for i in range (5):
        for k in range (5):
            if M[i+1][2*k]=="grey":
                if i+1==5:
                    N=N+2
                else:
                    N=N+1  
    for i in range (5):
        if M[0][2*i]=="grey":
            if i==0:
                if A[0][0]!="grey":
                    N=N+40
                if A[0][1]!="grey":
                    N=N+40
            if i==1:
                if A[0][0]!="grey":
                    N=N+40
                if A[0][2]!="grey":
                    N=N+40
            if i==3:
                if A[0][1]!="grey":
                    N=N+40
                if A[0][3]!="grey":
                    N=N+40
            if i==4:
                if A[0][2]!="grey":
                    N=N+40
                if A[0][3]!="grey":
                    N=N+40
            
    return N

def normecroix4 (M):
        N=0
        A=arrete(M)
        for i in range (6):
            for j in range(4):
                if A[i][j]== "grey":
                    a=arreteatache(M, i, j)[0]
                    b=arreteatache(M, i, j)[1]
                    if a!=b:
                        N=N+1
                    if i==5:
                        N=N+2
                    if i>=1 and i<=4:
                        if j==0 or j==3:
                            N=N+2
                        else:
                           N=N+1
        for i in range (5):
            for k in range (5):
                if M[i+1][2*k]=="grey":
                    if i+1==5:
                        N=N+2
                    else:
                        N=N+1  
        for i in range (5):
            if M[0][2*i]=="grey":
                if i==0:
                    if A[0][0]!="grey":
                        N=N+40
                    if A[0][1]!="grey":
                        N=N+40
                    if M[1][0]!="red":
                        N=N+40
                if i==1:
                    if A[0][0]!="grey":
                        N=N+40
                    if A[0][2]!="grey":
                        N=N+40
                    if M[3][2]!="orange":
                        N=N+40
                if i==3:
                    if A[0][1]!="grey":
                        N=N+40
                    if A[0][3]!="grey":
                        N=N+40
                    if M[2][0]!="blue":
                        N=N+40
                if i==4:
                    if A[0][2]!="grey":
                        N=N+40
                    if A[0][3]!="grey":
                        N=N+40
                    if M[3][0]!="orange":
                        N=N+40
                
        return N

def solvecroix1(M):
    Cubes=[[M,[]]]
    N=normecroix2(M)
    for x in range (20):
        print("sequence",x)
        Cubesbis=[]
        for k in Cubes:
            for i in range (12):
                if x==0:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix2(Mbis))
                    if normecroix2(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix2(Mbis)<=N+2 and normecroix2(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix2(Mbis)<N:
                        N=normecroix2(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if (x==1 and i%2==0 and i+1!=k[1][-1])or(x==1 and i%2==1 and i-1!=k[1][-1]):
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix2(Mbis))
                    if normecroix2(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix2(Mbis)<=N+2 and normecroix2(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix2(Mbis)<N:
                        N=normecroix2(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if x>1 and i%2==0 and i+1!=k[1][-1] and i!=k[1][-1]!=k[1][-2]:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix2(Mbis))
                    if normecroix2(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix2(Mbis)<=N+2 and normecroix2(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix2(Mbis)<N:
                        N=normecroix2(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if x>1 and i%2==1 and i-1!=k[1][-1] and i!=k[1][-1]!=k[1][-2]:
                     Mbis=makeamove(k[0],i)
                     M2bis=[]
                     M2bis.append(Mbis)
                     M2bis.append(k[1][:]) 
                     print(normecroix2(Mbis))
                     if normecroix2(Mbis)==0:
                             k[1].append(i)
                             k[0]=Mbis
                             print(k[1],"liste des coups")
                             printlistecoups(k[1])
                             printcube(Mbis)
                             print(Mbis)
                             return k
                     if normecroix2(Mbis)<=N+2 and normecroix2(Mbis)>=N :
                         M2bis[1].append(i)
                         Cubesbis.append(M2bis)
                     if normecroix2(Mbis)<N:
                         N=normecroix2(Mbis)
                         M2bis[1].append(i)
                         Cubesbis.append(M2bis)

        Cubes=Cubesbis
    print(Cubes[0][1])
    print(Cubes[0][0])
    print(N)
    return Cubes[0][1]  

def solvecroix2(M):
    Cubes=[[M,[]]]
    N=normecroix3(M)
    print(N)
    for x in range (20):
        Cubesbis=[]
        for k in Cubes:
            for i in range (12):
                if x==0:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix3(Mbis))
                    if normecroix3(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix3(Mbis)<=N+2 and normecroix3(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix3(Mbis)<N:
                        N=normecroix3(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if (x==1 and i%2==1 and i-1!=k[1][-1]) or (x==1 and i%2==0 and i+1!=k[1][-1]):
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix3(Mbis))
                    if normecroix3(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix3(Mbis)<=N+2 and normecroix3(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix3(Mbis)<N:
                        N=normecroix3(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if x>1 and i%2==0 and i+1!=k[1][-1] and i!=k[1][-1]!=k[1][-2]:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix3(Mbis))
                    if normecroix3(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix3(Mbis)<=N+2 and normecroix3(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix3(Mbis)<N:
                        N=normecroix3(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if x>1 and i%2==1 and i-1!=k[1][-1] and i!=k[1][-1]!=k[1][-2]:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix3(Mbis))
                    if normecroix3(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix3(Mbis)<=N+2 and normecroix3(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix3(Mbis)<N:
                        N=normecroix3(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                

        Cubes=Cubesbis
    print(Cubes[0][1])
    print(Cubes[0][0])
    print(N)
    for x in Cubes:
        if normecroix3(x[0])==N:
            printcube(x[0])
    
    return Cubes[0][1]  

def solvecroix3(M):
    Cubes=[[M,[]]]
    N=normecroix4(M)
    print(N)
    for x in range (25):
        Cubesbis=[]
        for k in Cubes:
            for i in range (12):
                if x==0:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix4(Mbis))
                    if normecroix4(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix4(Mbis)<=N+2 and normecroix4(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix4(Mbis)<N:
                        N=normecroix4(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if (x==1 and i%2==1 and i-1!=k[1][-1]) or (x==1 and i%2==0 and i+1!=k[1][-1]):
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix4(Mbis))
                    if normecroix4(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix4(Mbis)<=N+2 and normecroix4(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix4(Mbis)<N:
                        N=normecroix4(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if x>1 and i%2==0 and i+1!=k[1][-1] and i!=k[1][-1]!=k[1][-2]:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix4(Mbis))
                    if normecroix4(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix4(Mbis)<=N+2 and normecroix4(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix4(Mbis)<N:
                        N=normecroix4(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                if x>1 and i%2==1 and i-1!=k[1][-1] and i!=k[1][-1]!=k[1][-2]:
                    Mbis=makeamove(k[0],i)
                    M2bis=[]
                    M2bis.append(Mbis)
                    M2bis.append(k[1][:]) 
                    print(normecroix4(Mbis))
                    if normecroix4(Mbis)==0:
                            k[1].append(i)
                            k[0]=Mbis
                            print(k[1],"liste des coups")
                            printlistecoups(k[1])
                            printcube(Mbis)
                            print(Mbis)
                            return k
                    if normecroix4(Mbis)<=N+2 and normecroix4(Mbis)>=N :
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                    if normecroix4(Mbis)<N:
                        N=normecroix4(Mbis)
                        M2bis[1].append(i)
                        Cubesbis.append(M2bis)
                
    
        Cubes=Cubesbis
    print(Cubes[0][1])
    print(Cubes[0][0])
    print(N)
    for x in Cubes:
        if normecroix4(x[0])==N:
            printcube(x[0])
    
    return Cubes[0][1]  
    
def solveface(M):
    L=solvecroix1(M)
    L2=solvecroix3(L[0])
    print(L[1])
    print(L2[1])
    L3=L[1]+L2[1]
    print(L3)
    print(len(L3))
    printlistecoups(L3)
    return L3

def courronne_BO(B):
    A=arrete(B)
    M=B
    L=[]
    L1=[]
    if A[2][2]=="blue" and A[3][1]=="orange":
        return True
    if A[2][2]=="orange" and A[3][1]=="blue":
        L1=["D_1","R_1","D","R","D","F","D_1","F_1"]
        M=applycoup(M,listecoupnombre(L1))
    A=arrete(M)
    if A[2][2]!="blue" or A[3][1]!="orange":
        for i in range (4):
            if A[i+1][3]=="blue":
                if arreteatache(M, i+1, 3)[0]=="orange":
                    if i+1==1:
                        L=["D","D_1","R_1","D","R","D","F","D_1","F_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                        L=["D_1","R_1","D","R","D","F","D_1","F_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["D_1","D_1","R_1","D","R","D","F","D_1","F_1"] #attention ajouter les mouvement a trouver pour chacun 
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D","D","D_1","R_1","D","R","D","F","D_1","F_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
            if A[i+1][3]=="orange":
                if arreteatache(M, i+1, 3)[0]=="blue":
                    if i+1==1:
                        L=["D","D","D","F","D_1","F_1","D_1","R_1","D","R"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                       L=["D","D","F","D_1","F_1","D_1","R_1","D","R"]
                       return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["D","F","D_1","F_1","D_1","R_1","D","R"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D_1","D","F","D_1","F_1","D_1","R_1","D","R"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
    return False

def courronne_RB(B):
    

    A=arrete(B)
    M=B
    L=[]
    L1=[]
    if A[1][2]=="red" and A[2][1]=="blue":
        return True
    if A[1][2]=="blue" and A[2][1]=="red":
        L1=["D_1","F_1","D","F","D","L_1","D_1","L"]
        M=applycoup(M,listecoupnombre(L1))
    A=arrete(M)
    if A[2][2]!="red" or A[3][1]!="blue":
        for i in range (4):
            if A[i+1][3]=="red":
                if arreteatache(M, i+1, 3)[0]=="blue":
                    if i+1==1:
                        L=["D_1","F_1","D","F","D","L_1","D_1","L"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                        L=["D_1","D_1","F_1","D","F","D","L_1","D_1","L"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["D","F_1","D","F","D","L_1","D_1","L"] #attention ajouter les mouvement a trouver pour chacun 
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["F_1","D","F","D","L_1","D_1","L"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
            if A[i+1][3]=="blue":
                if arreteatache(M, i+1, 3)[0]=="red":
                    if i+1==1:
                        L=["D","D","L_1","D_1","L","D_1","F_1","D","F"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                       L=["D","L_1","D_1","L","D_1","F_1","D","F"]
                       return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["L_1","D_1","L","D_1","F_1","D","F"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D_1","L_1","D_1","L","D_1","F_1","D","F"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
    return False

def courronne_GR(B):
    A=arrete(B)
    M=B
    L=[]
    L1=[]
    if A[1][1]=="red" and A[4][2]=="green":
        return True
    
    if A[1][1]=="red" and A[4][2]=="green":
        L1=["B","D_1","B_1","D_1","L","D","L_1"]
        M=applycoup(M,listecoupnombre(L1))
    A=arrete(M)
    if A[1][1]!="red" or A[4][2]!="green":
        for i in range (4):
            if A[i+1][3]=="red":
                if arreteatache(M, i+1, 3)[0]=="green":
                    if i+1==1:
                        L=["D","B","D_1","B_1","D_1","L","D","L_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                        L=["B","D_1","B_1","D_1","L","D","L_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["D_1","B","D_1","B_1","D_1","L","D","L_1"] #attention ajouter les mouvement a trouver pour chacun 
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D","D","B","D_1","B_1","D_1","L","D","L_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
            if A[i+1][3]=="green":
                if arreteatache(M, i+1, 3)[0]=="red":
                    if i+1==1:
                        L=["D","D","L","D","L_1","D","B","D_1","B_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                       L=["D","L","D","L_1","D","B","D_1","B_1"]
                       return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["L","D","L_1","D","B","D_1","B_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D_1","L","D","L_1","D","B","D_1","B_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
    return False        

def courronne_OG(B):
    A=arrete(B)
    M=B
    L=[]
    L1=[]
    if A[3][2]=="orange" and A[4][1]=="green":
        return True
    if A[3][2]=="green" and A[4][1]=="orange":
        L1=["B_1","D","B","D","R","D_1","R_1"]
        M=applycoup(M,listecoupnombre(L1))
    
    A=arrete(M)
    if A[3][2]!="orange" or A[4][1]!="green":
        for i in range (4):
            if A[i+1][3]=="orange":
                if arreteatache(M, i+1, 3)[0]=="green":
                    if i+1==1:
                        L=["D","B_1","D","B","D","R","D_1","R_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                        L=["B_1","D","B","D","R","D_1","R_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["D_1","B_1","D","B","D","R","D_1","R_1"] #attention ajouter les mouvement a trouver pour chacun 
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D","D","B_1","D","B","D","R","D_1","R_1"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
            if A[i+1][3]=="green":
                if arreteatache(M, i+1, 3)[0]=="orange":
                    if i+1==1:
                        L=["R","D_1","R_1","D_1","B_1","D","B"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==2:
                       L=["D_1","R","D-1","R_1","D_1","B_1","D","B"]
                       return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==3:
                        L=["D","D","R","D_1","R_1","D_1","B_1","D","B"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
                    if i+1==4:
                        L=["D","R","D_1","R_1","D_1","B_1","D","B"]
                        return [L1+L,applycoup(M,listecoupnombre(L))]
    return False      

def courronne(A):

#bien refaire courronne sinon il y aura un problème
    M=A
    L=[]
    n=0
    while courronne_BO(M)!=True or courronne_GR(M)!=True or courronne_OG(M)!=True or courronne_RB(M)!=True:
            n=n+1
            
            if courronne_BO(M)!=False:
                if courronne_BO(M)!=True:
                    n=0
                    L=L+courronne_BO(M)[0]
                    M=courronne_BO(M)[1]
                    print("BO fait")
            if courronne_GR(M)!=False:
                if courronne_GR(M)!=True:
                    n=0
                    L=L+courronne_GR(M)[0]
                    M=courronne_GR(M)[1]
                    print("GR fait")
            if courronne_OG(M)!=False:
                if courronne_OG(M)!=True:
                    n=0
                    L=L+courronne_OG(M)[0]
                    M=courronne_OG(M)[1]
                    print("OG fait")
            if courronne_RB(M)!=False:
                if courronne_RB(M)!=True:
                    n=0
                    L=L+courronne_RB(M)[0]
                    M=courronne_RB(M)[1]
                    print("RB fait")
            if n==4:
                if courronne_BO(M)!=True:
                    n=0
                    L=L+["D_1","R_1","D","R","D","F","D_1","F_1"]
                    M=applycoup(M,listecoupnombre(["D_1","R_1","D","R","D","F","D_1","F_1"]))
                elif courronne_RB(M)!=True:
                    n=0
                    L=L+["D_1","F_1","D","F","D","L_1","D_1","L"]
                    M=applycoup(M,listecoupnombre(["D_1","F_1","D","F","D","L_1","D_1","L"]))
                elif courronne_GR(M)!=True:
                    n=0
                    L=L+["B","D_1","B_1","D_1","L","D","L_1"]
                    M=applycoup(M,listecoupnombre(["B","D_1","B_1","D_1","L","D","L_1"]))
                elif courronne_OG(M)!=True:
                    n=0
                    L=L+["B_1","D","B","D","R","D_1","R_1"]
                    M=applycoup(M,listecoupnombre(["B_1","D","B","D","R","D_1","R_1"]))
    printcube(M)
    return [L,M]

#bien refaire courronne sinon il y aura un problème
    M=A
    L=[]
    if courronne_BO(M)==False and courronne_GR(M)==False and courronne_OG(M)==False and courronne_RB(M)==False:
        L=["D","D_1","R_1","D","R","D","F","D_1","F_1"]
        print("cas compliqué")#refaire ici
        M=applycoup(M, listecoupnombre(L))
    while courronne_BO(M)!=True or courronne_GR(M)!=True or courronne_OG(M)!=True or courronne_RB(M)!=True:
            if courronne_BO(M)!=False:
                if courronne_BO(M)!=True:
                    L=L+courronne_BO(M)[0]
                    M=courronne_BO(M)[1]
                    print("BO fait")
            if courronne_GR(M)!=False:
                if courronne_GR(M)!=True:
                    L=L+courronne_GR(M)[0]
                    M=courronne_GR(M)[1]
                    print("GR fait")
            if courronne_OG(M)!=False:
                if courronne_OG(M)!=True:
                    L=L+courronne_OG(M)[0]
                    M=courronne_OG(M)[1]
                    print("OG fait")
            if courronne_RB(M)!=False:
                if courronne_RB(M)!=True:
                    L=L+courronne_RB(M)[0]
                    M=courronne_RB(M)[1]
                    print("RB fait")
    printcube(M)
    return [L,M]



def solvecourrone(M):
    A=M
    L=solveface(M)
    print(len(L))
    A=applycoup(M, L)
    print("A",A)
    L1=courronne(A)
    print("L1",L1)
    L=printlistecoups(L)+L1[0]
    print(len(L))
    H=[L,L1[1]]
    return H

#print("voila le résultat" ,solvecourrone(B))
T=[['grey', 'grey', 'grey', 'grey', 'grey', 'grey', 'grey', 'grey', 'grey'],
   ['red', 'red', 'red', 'orange', 'red', 'yellow', 'yellow', 'green', 'blue'], 
   ['blue', 'blue', 'blue', 'green', 'blue', 'yellow', 'yellow', 'red', 'yellow'],
   ['orange', 'orange', 'orange', 'orange', 'orange', 'yellow', 'orange', 'blue', 'yellow'],
   ['green', 'green', 'green', 'red', 'green', 'blue', 'green', 'blue', 'orange'],
   ['red', 'green', 'green', 'orange', 'yellow', 'red', 'blue', 'yellow', 'red']]
"""
L=['R_1', 'U_1', 'F_1', 'D_1', 'R', 'F_1', 'L', 'F_1', 'D', 'F_1', 'D_1', 'B_1', 'D_1', 'B', 'F_1', 'D_1', 'F_1', 'D', 'L_1', 'D_1', 'L', 'D_1', 'B', 'D_1', 'B_1', 'B', 'D_1', 'B_1', 'D_1', 'L', 'D', 'L_1', 'B_1', 'D', 'B', 'D', 'R', 'D_1', 'R_1', 'L_1', 'D_1', 'L', 'D_1', 'F_1', 'D', 'F', 'D_1', 'D_1', 'R_1', 'D', 'R', 'D', 'F', 'D_1', 'F_1']
print(len(L))
print(listecoupnombre(L),len(listecoupnombre(L)))
print(solveface(B))

"""
B=[['grey', 'blue', 'grey', 'yellow', 'grey', 'grey', 'grey', 'orange', 'red'],
   ['orange', 'red', 'orange', 'green', 'red', 'orange', 'yellow', 'orange', 'blue'], 
   ['green', 'yellow', 'green', 'grey', 'blue', 'green', 'red', 'red', 'green'],
   ['yellow', 'green', 'green', 'yellow', 'orange', 'green', 'yellow', 'blue', 'yellow'],
   ['red', 'red', 'blue', 'red', 'green', 'orange', 'red', 'blue', 'orange'], 
   ['grey', 'grey', 'orange', 'blue', 'yellow', 'yellow', 'blue', 'grey', 'blue']]

Bcouronne=[['grey', 'grey', 'grey', 'grey', 'grey', 'grey', 'grey', 'grey', 'grey'],
           ['red', 'red', 'red', 'red', 'red', 'red', 'green', 'yellow', 'blue'],
           ['blue', 'blue', 'blue', 'blue', 'blue', 'blue', 'yellow', 'blue', 'yellow'],
           ['orange', 'orange', 'orange', 'orange', 'orange', 'orange', 'blue', 'yellow', 'green'],
           ['green', 'green', 'green', 'green', 'green', 'green', 'yellow', 'orange', 'yellow'],
           ['red', 'yellow', 'orange', 'green', 'yellow', 'red', 'red', 'yellow', 'orange']]

#partie OLL



def infofacejaune(M):
    L=[]
    for i in range (4):
        for j in range (3):
            if M[i+1][j]== 'yellow' :
                L.append(i*3+j)
    for i in range (9):
        if M[0][i]=='yellow':
            L.append(12+i)
    return L

#oll prise dans le doc oll-algorithme

OLL=[ #all edges oriented correctly
     [[0,3,6,13,14,15,16,17,19],["R","U","U","R_1","U_1","R", "U_1","R_1"]],
     [[5,8,11,13,15,16,17,18,19],["R","U","R_1","U","R","U","U","R_1"]],
     [[3,5,9,11,13,15,16,17,19],["R","U","U","R_1","U_1","R","U","R_1","U_1","R","U_1","R_1"]],
     [[0,2,5,9,13,15,16,17,19],["R","U","U","R","R","U_1","R","R","U_1","R","R","U","U","R"]],
     [[3,11,13,14,15,16,17,19,20],["L_1","F","R_1","F_1","L","F","R","F_1"]],
     [[3,8,12,13,15,16,17,19,20],["R_1","F","R","B_1","R_1","F_1","R","B"]],
     [[3,5,12,13,14,15,16,17,19],["R","R","D","R_1","U","U","R","D_1","R_1","U","U","R_1"]],
     #tshape
     [[3,4,10,11,14,15,16,17,20],["R","U","R_1","U_1","R_1","F","R","F_1"]],
     [[0,2,4,10,14,15,16,17,20],["F","R","U","R_1","U_1","F_1"]],
     #square
     [[1,2,8,10,11,16,17,19,20],["L","B","B","R","B","R_1","B","L_1"]],
     [[0,1,3,4,6,13,14,16,17],["L_1","F","F","R_1","F_1","R","F_1","L"]],
     #C-shape
     [[0,4,8,10,15,16,17,20],["R","U","R","R","U_1","R_1","F","R","U","R","U_1","F_1"]],
     [[1,6,7,8,12,13,16,18,19],["R_1","U_1","R_1","F","R","F_1","U","R"]],
     #W-shapes 
     [[3,7,8,10,12,15,16,19,20],["L_1","U","L","B_1","L_1","U","L","U_1","L","B","L_1","U_1","L","B","L_1","B_1"]],
     [[4,6,7,11,13,14,15,16,18],["R","U","R_1","U","R","U_1","R_1","U_1","R_1","F","R","F_1"]],
     #corner correct edges flipped 
     [[4,7,12,13,14,15,16,18,20],["L_1","F","R_1","F_1","R","L","D","R","D_1","R_1"]],
     [[4,10,12,14,15,16,17,18,20],["R","U","R_1","U_1","R_1","L_1","F","R","F_1","L"]],
     
     #P-Shapes
     [[1,3,4,11,13,14,16,17,20],["R_1","U_1","F","U","R","U_1","R_1","F_1","R"]],
     [[1,3,10,11,14,16,17,19,20],["R","U","B_1","U_1","R_1","U","R","B","R_1"]],
     [[6,7,8,10,12,15,16,18,19],["B_1","U_1","R_1","U","R","B"]],
     [[0,1,2,10,14,16,17,19,20],["B","U","L_1","U_1","L","B_1"]],
     #I-shape
     [[0,2,4,5,9,10,15,16,17],["B","U","L_1","U_1","L","U","L_1","U_1","L","B_1"]],
     [[0,2,4,6,8,10,15,16,17],["L","B_1","L_1","U_1","R_1","U","R","U_1","R_1","U","R","L","B","L_1"]],
     [[1,3,6,7,8,11,13,16,19],["R_1","U_1","R","U_1","R_1","U","F_1","U","F","R"]],
     [[0,1,2,6,7,8,13,16,19],["B_1","R","B","U","B","U_1","B","B","R_1","B","B","U_1","B_1","U","B","U","B_1"]],
     #fish shapes
     [[0,3,4,7,9,13,15,16,20],["R","U","R_1","U_1","R_1","F","R","R","U","R_1","U_1","F_1"]],
     [[2,5,7,10,11,14,15,16,19],["R","U","R_1","U","R_1","F","R","F_1","R","U","U","R_1"]],
     [[1,3,8,10,12,16,17,19,20],["R","U","U","R","R","F","R","F_1","R","U","U","R_1"]],
     [[3,4,7,8,12,13,15,16,20],["F","R","U_1","R_1","U_1","R","U","R_1","F_1"]],
     #knight move shape 
     [[4,5,8,10,11,15,16,17,18],["F","U","R","U_1","R","R","F_1","R","U","R","U_1","R_1"]],
     [[0,3,4,9,10,15,16,17,20],["R_1","F","R","U","R_1","F_1","R","F","U_1","F_1"]],
     [[0,3,4,6,10,14,15,16,17],["L_1","F","L","R","U","R_1","U_1","L_1","F_1","L"]],
     [[2,4,8,10,11,15,16,17,20],["L","B-1","L_1","R_1","U_1","R","U","L","B","L_1"]],
     #awkward shape tout chec jusque ici 
     [[2,6,7,10,12,14,15,16,19],["B","U","B_1","U_1","B","U_1","B_1","R_1","U_1","R","B","U","B_1"]],
     [[1,4,5,9,12,13,16,17,18],["L_1","U","F","U","U","F_1","U_1","F","U","U","F_1","U_1","L"]],
     [[4,7,9,11,13,15,16,18,20],["R","U","R_1","U","R","U","U","R_1","F","R","U","R_1","U_1","F_1"]],
     [[3,5,7,10,12,14,15,16,19],["R_1","U_1","R","U_1","R_1","U","U","R","F","R","U","R_1","U_1","F_1"]],
     #L shape
     [[0,2,4,5,7,9,13,15,16],["F","R","U","R_1","U_1","R","U","R_1","U_1","F_1"]],
     [[1,3,4,6,8,11,13,16,17,],["F_1","L","U_1","L_1","U","L","U_1","L_1","U","F"]],
     [[0,1,2,4,5,9,13,16,17],["L_1","F_1","L_1","L_1","B","L_1","L_1","F","L_1","L_1","B_1","L_1"]],
     [[0,1,2,5,9,10,16,17,19],["L","B","L","L","F_1","L","L","B_1","L","L","F","L"]],
     [[0,1,2,6,8,10,16,17,19],["L","B_1","R","B_1","R_1","B","R","B_1","R_1","B","B","L_1"]],
     [[0,1,2,6,8,10,16,17,19],["L_1","F","R_1","F","R","F_1","R_1","F","R","F","F","L"]],
     #lightning Bolts
     [[4,5,7,8,11,13,15,16,18],["L_1","F","R_1","F","R","F","F","L"]],
     [[3,6,7,9,10,12,15,16,19],["L","B_1","R","B_1","R_1","B","B","L_1"]],
     [[1,5,8,10,11,16,17,18,19],["L","R","R","B","R_1","B","R","B","B","R_1","B","R_1","L_1"]],
     [[1,3,4,6,9,12,13,16,17],["L_1","R_1","R_1","F_1","R","F_1","R_1","F","F","R","F_1","R","L"]],
     [[4,6,10,14,15,16,17,18],["L_1","F_1","L","U_1","L_1","U","F","U_1","L"]],
     [[2,4,9,10,12,15,16,17,20],["R_1","F","R","U","R_1","U_1","F_1","U","R"]],
     #No edges flipped correctly
     [[0,1,2,4,6,7,8,10,16],["R","U","U","R","R","F","R","F_1","U","U","R_1","F","R","F_1"]],
     [[0,1,2,4,5,7,9,10,16],["F","R","U","R_1","U_1","F_1","B","U","L_1","U_1","L","B_1"]],
     [[1,2,4,7,8,10,11,16,20],["B","U","L_1","U_1","L","B_1","U_1","F","R","U","R_1","U_1","F_1"]],
     [[0,1,3,4,6,7,10,14,16],["B","L_1","U_1","L","B_1","U","F","R","U","R_1","U_1","F_1"]],
     [[1,3,4,5,7,10,12,14,16],["B","U","U","B","B","R","B","R_1","U","U","B","F_1","L_1","B","L","F_1"]],
     [[1,2,4,6,7,10,12,14,16],["R","L","B","R","B","R_1","B_1","R_1","L_1","R_1","F","R","F_1"]],
     [[1,2,4,7,9,10,12,16,20],["R","U","R_1","U","R_1","F","R","F_1","U","U","R_1","F","R","F_1"]],
     [[1,4,7,10,12,14,16],["R","L","B","R","B","R_1","B_1","R","R","L","L","F","R","F_1","L"]]
     ]
     
     
def oll(M):
    A=retournecube(M)
    L=infofacejaune(A)
    
    listecoup=["T"]
    print(L)
    for i in range(4):
        if i!=0:
            listecoup.append("Y")
            print("y")
            A=y(A)
        L=infofacejaune((A))
        print(L)
        for x in OLL:
            if x[0]==L:
                print(infofacejaune(A))
                A=applycoup(A, listecoupnombre(x[1]))
                printcube(A)
                H=[listecoup+x[1],A]
                return H
   
def solveoll(M):

    L=solvecourrone(M)
    L2=oll(L[1])
    print(L2)
    print(L[0])
    L[0]=L[0]+L2[0]
    L[1]=L2[1]
    printcube(L[1])
    print(L[0])
    print(len(L[0]))
    return L
   
    

def formepll(M):
    L=[]
    Lcoin=[]
    Larrete=[]
    Base=[M[1][4],M[2][4],M[3][4],M[4][4]]
    for i in range (4):
        for j in range(4):
            if M[i%4+1][2]==Base[j%4] and M[(i+1)%4+1][0]==Base[(j+1)%4]:
                Lcoin.append(j+1)
    for i in range(4):
        for j in range(4):
            if M[i%4+1][1]==Base[j%4]:
                Larrete.append(j+1)
    for i in range(4):
        L.append(1+2*(Larrete[i]-1))
        L.append(2*Lcoin[i])
    return L
            
            
PLL=[
     #d'arrete
     [[3,2,5,4,1,6,7,8],["R","U_1","R","U","R","U","R","U_1","R_1","U_1","R","R"]],
     [[5,2,1,4,3,6,7,8],["R","R","U","R","U","R_1","U_1","R_1","U_1","R_1","U","R_1"]],
     [[5,2,7,4,1,6,3,8],["R","L","R","L","D","R","L","R","L","U","U","R","L","R","L","D","R","L","R","L"]],
     [[7,2,5,4,3,6,1,8],["R_1","U_1","R","U_1","R","U","R","U_1","R_1","U","R","U","R","R","U_1","R_1","U_1","U","U"]],
     #De coin
     [[1,2,3,8,5,4,7,6],["R_1","F","R_1","B","B","R","F_1","R_1","B","B","R","R"]],
     [[1,4,3,6,5,2,7,8],["R","B_1","R","F","F","R_1","B","R","F","F","R","R"]],
     [[1,8,2,6,5,4,7,2],["R","F_1","R_1","B","R","F","R_1","B","D_1","B","D","F","D_1","B_1","R"]],
     #les j
     [[1,2,5,6,3,4,7,8],["R","U","R_1","F_1","R","U","R_1","U_1","R_1","F","R","R","U_1","R_1","U_1"]],
     [[7,2,3,4,5,8,1,6],["R_1","U","L","U","U","R","U_1","R_1","U","U","R","L_1","U_1"]],
     #les R
     [[1,2,5,4,3,8,7,6],["R_1","U","U","R","U","U","R_1","F","R","U","R_1","U_1","R_1","F_1","R","R","U_1"]],
     [[3,2,1,4,5,8,7,6],["R","U","U","R_1","U","U","R","B_1","R_1","U_1","R","U","R","B"]],
     #les G
     [[3,8,6,4,5,2,1,6],["R","U","R_1","F","F","D_1","L_1","U_1","L","U","L","D","F","F"]],
     [[7,4,1,8,5,6,3,2],["R_1","U_1","R","B","B","D","L","U","L_1","U_1","L_1","D_1","B","B"]],
     [[5,4,1,8,3,6,7,2],["R","R","D_1","F","U_1","F","U","F_1","D","R","R","B","U_1","B_1"]],
     [[5,8,3,4,7,2,1,6],["R","R","D","B_1","U","B_1","U_1","B","D_1","R","R","F_1","U","F"]],
     #Les autres
     [[5,2,3,6,1,4,7,8],["R","U","R_1","U_1","R_1","F","R","R","U_1","R","U_1","R","U","R_1","F_1"]],
     [[7,2,3,8,5,6,1,4],["F","R","U_1","R_1","U_1","R","U","R_1","F_1","R","U","R_1","U_1","R_1","F","R","F_1"]],
     [[1,2,3,8,7,6,5,4],["R_1","U","R_1","U_1","F_1","L","F","F","U_1","F_1","U","F_1","L_1","F","L_1"]],
     [[5,4,3,2,1,6,7,8],["R_1","U","U","R_1","U_1","B_1","R_1","B","B","U_1","B_1","U","B_1","R","B","U_1","R"]],
     [[1,6,7,4,5,2,3,8],["R_1","U","L","U","U","R","U_1","L_1","R_1","U","L","U","U","R","U_1","L_1","U_1"]],
     [[1,2,7,8,5,6,3,4],["L_1","U_1","R","U","U","L","U","R_1","L_1","U_1","R","U","U","L","U","R_1","U"]]
     ]       
            
def pll(M):
    A=M
    L=formepll(M)
    Listecoup=[]
    print(L)
    n=0
    for i in range (4):
        if n!=0:
            n=n+1
            A=y(A)
            L=formepll(A)
            print(L)
            Listecoup.append("Y")
            for x in PLL:
                if x[0]==L:
                    print("il a trouvé en ",i)
                    Listecoup=Listecoup+x[1]
                    A=applycoup(A, listecoupnombre(x[1]))
                    printcube(A)
                    return [Listecoup,A]
        if n==0:
            n=n+1
            L=formepll(A)
            print(L)
            for x in PLL:
                if x[0]==L:
                    print("il a trouvé en ",i)
                    Listecoup=x[1]
                    A=applycoup(A, listecoupnombre(x[1]))
                    printcube(A)
                    return [Listecoup,A]
            



def pll2(M):
    A=M
    L=[]
    for i in range(4):
        H=pll(A)
        if H==None:
            print("il a du tourner")
            L.append("U")
            A=moveU(A)
        else:
            L=L+H[0]
            return [L,H[1]]
            


def fonctionultime(M):
    L=solveoll(M)
    L2=pll2(L[1])
    L[0]=L[0]+L2[0]
    L[1]=L2[1]
    printcube(L[1])
    print(L[0])
    print(len(L[0]))
    return L

Tesseract=[['green', 'green', 'blue', 'green', 'grey', 'blue', 'orange', 'green', 'red'], ['orange', 'orange', 'grey', 'red', 'red', 'red', 'orange', 'orange', 'red'], ['blue', 'grey', 'grey', 'blue', 'blue', 'blue', 'grey', 'blue', 'red'], ['green', 'orange', 'red', 'grey', 'orange', 'yellow', 'yellow', 'grey', 'yellow'], ['yellow', 'yellow', 'grey', 'red', 'green', 'green', 'orange', 'grey', 'blue'], ['blue', 'yellow', 'green', 'yellow', 'yellow', 'red', 'yellow', 'orange', 'green']]
printcube(Tesseract)
"""printcube(applycoup(Tesseract, listecoupnombre(['R', 'F_1', 'R_1', 'L', 'B', 'U', 'F_1', 'R_1', 'L_1', 'D_1', 'L', 'F_1', 'D_1', 'F', 'D_1', 'L', 'D_1', 'L_1', 'F', 'D_1', 'F_1', 'R', 'D', 'R_1', 'D_1', 'R', 'D', 'R_1', 'D', 'D', 'B', 'D_1', 'B_1', 'D_1', 'L', 'D', 'L_1', 'D_1', 'F_1', 'D', 'F', 'D', 'L_1', 'D_1', 'L', 'D', 'D', 'F', 'D_1', 'F_1', 'D_1', 'R_1', 'D', 'R', 'D', 'D', 'R', 'D_1', 'R_1', 'D_1', 'B_1', 'D', 'B', 'T', 'Y', 'Y', 'Y', 'L_1', 'F', 'R_1', 'F_1', 'L', 'F', 'R', 'F_1', 'U', 'Y', 'R', 'U', 'R_1', 'F_1', 'R', 'U', 'R_1', 'U_1', 'R_1', 'F', 'R', 'R', 'U_1', 'R_1', 'U_1'])))
"""
plt.show()