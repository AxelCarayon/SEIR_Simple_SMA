from outputToGraph import run_java_jar, make_diagram, JAR_LOCATION,OUTPUT_FILE_LOCATION
import sys
import shutil

def save_csv(i) :
    old_location = OUTPUT_FILE_LOCATION
    new_location = f"src/main/resources/output{i}.csv"
    shutil.move(old_location,new_location)

if ((len(sys.argv) > 1)):
    for i in range (int(sys.argv[1])) :
        run_java_jar(JAR_LOCATION)
        make_diagram(OUTPUT_FILE_LOCATION)
        save_csv(i)
else :
    print("Usage : python3 multipleRuns.py <nb of runs>")