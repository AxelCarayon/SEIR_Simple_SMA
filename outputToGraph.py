import csv
import yaml
import matplotlib.pyplot as plt

OUTPUT_FILE_LOCATION = 'src/main/resources/output.csv'
OUTPUT_FOLDER = 'src/main/resources/pythonOutput'
JAR_LOCATION = 'out/artifacts/SMA_SEIR_jar/SMA-SEIR.jar'
YAML_FILE = 'src/main/resources/parameters.yaml'


def read_csv(filename):
    with open(filename, 'r') as csvfile:
        reader = csv.reader(csvfile)
        return list(reader)


def get_values():
    with open(YAML_FILE, 'r') as file:
        data = yaml.safe_load(file)
        incubation = data['incubationRate']
        infection = data['infectionRate']
        recovery = data['recoveryRate']
        loose_immunity = data['looseImmunityRate']
    return f"incubationRate : {incubation} InfectionRate : {infection}\n " \
           f"RecoveryRate : {recovery} LooseImmunityRate : {loose_immunity}"


def make_diagram(filename):
    data = read_csv(filename)
    suceptible = []
    exposed = []
    recovered = []
    infected = []

    for row in data[1:]:
        suceptible.append(int(row[0]))
        exposed.append(int(row[1]))
        recovered.append(int(row[2]))
        infected.append(int(row[3]))

    plt.title(get_values())
    plt.plot(suceptible, label='Suceptible', color='gray')
    plt.plot(exposed, label='Exposed', color='yellow')
    plt.plot(infected, label='Infected', color='red')
    plt.plot(recovered, label='Recovered', color='green')
    plt.xlabel('Cycles')
    plt.ylabel('Peoples')
    plt.legend()
    plt.savefig(f'{filename.split(".")[0]}.png')
    # plt.show()
    plt.close()


def run_java_jar(filename):
    import subprocess
    subprocess.call(['java', '-jar', filename])


def copy_to_output_folder(filename):
    import shutil
    shutil.copy(OUTPUT_FILE_LOCATION, f"{OUTPUT_FOLDER}/{filename}")


def create_file():
    with open(OUTPUT_FILE_LOCATION, 'w') as file:
        file.write('')


def edit_yaml(key, value):
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

run_java_jar(JAR_LOCATION)
make_diagram(OUTPUT_FILE_LOCATION)
