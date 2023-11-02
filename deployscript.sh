# Improvised from: https://github.com/ThomasOM/MavenRepoTool

# Verify arguments
set -e
if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]
  then
    echo "Usage: deployscript <group_id> <artifact_id> <version>"
    exit 1
fi

echo "Starting deploy script!"

# Startup arguments
group_id=${1}
artifact_id=${2}
version=${3}

# Clean install in base folder to ensure the artifact is present
mvn clean install

# Create new temporary repository directory, ".repo"
base_dir=$(pwd)
target_dir=${base_dir}/target
repo_dir=${base_dir}/.repo

# Copy git folder if the directory didn't exist yet
if ! [[ -d ${repo_dir} ]]
then
  mkdir ${repo_dir}
  echo "Copying git folder from original project..."
  cp -a ${base_dir}/.git ${repo_dir}/.git
fi

# Check out to git branch "repository"
echo "Setting up repository branch and installing jar..."
cd ${repo_dir}
git checkout -B repository

# Create maven library in ".repo"
jar_location=${target_dir}/${artifact_id}-${version}.jar
javadoc_location=${target_dir}/${artifact_id}-${version}-javadoc.jar
sources_location=${target_dir}/${artifact_id}-${version}-sources.jar
mvn install:install-file -DlocalRepositoryPath="." -Dpackaging="jar" -DcreateChecksum="true" -DgeneratePom="true" \
  -DgroupId="${group_id}" \
  -DartifactId="${artifact_id}" \
  -Dversion="${version}" \
  -Dfile="${jar_location}" \
  -Djavadoc="${javadoc_location}" \
  -Dsources="${sources_location}" \

echo -e "# Github Maven Repository" > README.md

echo "Committing and pushing to repository branch..."
git add -A .
git commit -m "Release version ${version}"
git push origin repository --force

echo "Deleting temporary folder..."
rm -rf $repo_dir

echo "Done!"