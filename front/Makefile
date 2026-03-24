ELM = elm
SRC_DIR = src
BUILD_DIR = build
TARGET = $(BUILD_DIR)/elm.js

all: $(BUILD_DIR) $(TARGET)

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

$(TARGET): $(SRC_DIR)/Main.elm $(SRC_DIR)/*
	$(ELM) make $< --output=$@

clean:
	rm -rf $(BUILD_DIR)
