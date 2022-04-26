import csv
import yaml
import matplotlib.pyplot as plt

OUTPUT_FILE_LOCATION = 'src/main/resources/output.csv'
OUTPUT_FOLDER = 'src/main/resources/pythonOutput'
JAR_LOCATION = 'out/artifacts/SMA_SEIR_jar/SMA-SEIR.jar'
YAML_FILE = 'src/main/resources/parameters.yaml'

def readCSV(fileName):
    with open(fileName, 'r') as csvfile:
        reader = csv.reader(csvfile)
        return list(reader)

def getValues() :
    with open(YAML_FILE, 'r') as file:
        data = yaml.safe_load(file)
        incubation = data['incubationRate']
        infection = data['infectionRate']
        recovery = data['recoveryRate']
        looseImmunity = data['looseImmunityRate']
    return f"incubationRate : {incubation} InfectionRate : {infection}\n RecoveryRate : {recovery} LooseImmunityRate : {looseImmunity}"

def makeDiagram(fileName):
    data = readCSV(fileName)
    suceptible = []
    exposed = []
    recovred = []
    infected = []

    for row in data[1:]:
        suceptible.append(int(row[0]))
        exposed.append(int(row[1]))
        recovred.append(int(row[2]))
        infected.append(int(row[3]))
    
    plt.title(getValues())
    plt.plot(suceptible, label='Suceptible', color='gray')
    plt.plot(exposed, label='Exposed', color='yellow')
    plt.plot(infected, label='Infected', color='red')
    plt.plot(recovred, label='Recovered', color='green')
    plt.xlabel('Cycles')
    plt.ylabel('Peoples')
    plt.legend()
    plt.savefig(f'{fileName.split(".")[0]}.png')
    #plt.show()
    plt.close()

def runJavaJar(fileName):
    import subprocess
    subprocess.call(['java', '-jar', fileName])

def copyToOutputFolder(fileName):
    import shutil
    shutil.copy(OUTPUT_FILE_LOCATION, f"{OUTPUT_FOLDER}/{fileName}")
    
def createFile():
    with open(OUTPUT_FILE_LOCATION, 'w') as file:
        file.write('')

def editYaml(key,value):
    with open(YAML_FILE, 'r') as file:
        data = yaml.safe_load(file)
    data[key] = value
    
    with open(YAML_FILE, 'w') as file:
        yaml.dump(data, file)

# if __name__ == "__main__":
#     for i in range(10):
#         editYaml("infectionRate", 0.05+(0.05*i))
#         runJavaJar(JAR_LOCATION)
#         copyToOutputFolder(f"output{i}.csv")
#         makeDiagram(f"{OUTPUT_FOLDER}/output{i}.csv")

runJavaJar(JAR_LOCATION)
makeDiagram(OUTPUT_FILE_LOCATION)