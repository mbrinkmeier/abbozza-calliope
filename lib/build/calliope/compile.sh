BUILD_PATH=`dirname $0`
if [ ! -e "$BUILD_PATH" ]; then
  case $BUILD_PATH in
    (*/*) exit 1;;
    (*) BUILD_PATH=$(command -v -- "$BUILD_PATH") || exit;
  esac
fi
dir=$(
  cd -P -- "$(dirname -- "$BUILD_PATH")" && pwd -P
) || exit
BUILD_PATH=$dir/$(basename -- "$BUILD_PATH") || exit 

$BUILD_PATH

cd $BUILD_PATH
yt build

### Working variant for bash 
#
# BUILD_PATH="`dirname \"$0\"`"
# BUILD_PATH="`( cd \"$BUILD_PATH\" && pwd )`"
# 
# cd $BUILD_PATH
# yt build
