FRONT =	front
FRONT_OUT = $(FRONT)/build/elm.js
JS = src/main/resources/static/elm.js

all: delete $(JS) package

package:
	mvn package

$(JS):
	$(MAKE) -C $(FRONT)
	cp $(FRONT_OUT) $(JS)

delete:
	rm -rf $(JS)

clean:
	$(MAKE) -C $(FRONT) clean
	mvn clean
