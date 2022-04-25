import csv
import matplotlib.pyplot as plt

def readCSV(fileName):
    with open(fileName, 'r') as csvfile:
        reader = csv.reader(csvfile)
        return list(reader)

def showDiagram(data):
    suceptible = []
    exposed = []
    recovred = []
    infected = []

    for row in data[1:]:
        suceptible.append(int(row[0]))
        exposed.append(int(row[1]))
        recovred.append(int(row[2]))
        infected.append(int(row[3]))
    
    plt.plot(suceptible, label='Suceptible', color='gray')
    plt.plot(exposed, label='Exposed', color='yellow')
    plt.plot(infected, label='Infected', color='red')
    plt.plot(recovred, label='Recovered', color='green')
    plt.xlabel('Cycles')
    plt.ylabel('Peoples')
    plt.legend()
    plt.show()

def runJavaJar(fileName):
    import subprocess
    subprocess.call(['java', '-jar', fileName])
    
runJavaJar('out/artifacts/SMA_SEIR_jar/SMA-SEIR.jar')

# data = readCSV("src/main/resources/output.csv")

# showDiagram(data)