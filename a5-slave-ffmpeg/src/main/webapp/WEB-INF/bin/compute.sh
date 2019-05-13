#!/bin/bash
#
#SBATCH --job-name=test
#SBATCH --output=res.txt
#
#SBATCH --ntasks=1
#SBATCH --time=10:00
#SBATCH --mem-per-cpu=100

for i in "$@"
do
case $i in
    -c=*)
    COMMAND="${i#*=}"
    shift;;
esac
done

srun ${COMMAND}
srun sleep 60
