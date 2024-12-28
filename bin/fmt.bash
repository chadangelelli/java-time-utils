
JAVA_TIME_UTILS_LOGO=$(cat <<'JAVA_TIME_UTILS_LOGO_TEXT'
     _                 _____ _             _   _ _   _ _
  _ | |__ ___ ____ _  |_   _(_)_ __  ___  | | | | |_(_) |___
 | || / _` \ V / _` |   | | | | '  \/ -_) | |_| |  _| | (_-<
  \__/\__,_|\_/\__,_|   |_| |_|_|_|_\___|  \___/ \__|_|_/__/
JAVA_TIME_UTILS_LOGO_TEXT
)

BOLD='\033[1m'
ITAL='\033[3m'
BLUE='\033[0;34m'
RED='\033[0;31m'
GREEN='\033[0;32m'
ORANGE='\033[0;33m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

print_logo() {
    echo -e "${BLUE}${JAVA_TIME_UTILS_LOGO}${NC}"
}

