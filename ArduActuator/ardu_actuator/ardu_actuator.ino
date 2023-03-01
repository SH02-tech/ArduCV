#define LMOTOR 11
#define RMOTOR 13
#define BAUDRATE 9600
#define DEFAULTSIGNAL 0
#define DELAYMSEG 100

char t = DEFAULTSIGNAL;
 
void setup() {
  pinMode(RMOTOR, OUTPUT);   
  pinMode(LMOTOR, OUTPUT);   
  Serial.begin(BAUDRATE);
}
 
void loop() {

  if (!Serial.available()) {
    digitalWrite(LMOTOR, LOW);
    digitalWrite(RMOTOR, LOW);

    while(!Serial.available()); // Esperamos hasta que haya algún mensaje. 
  }

  t = Serial.read();

  int lstatus = (t == 'F' || t == 'L') ? HIGH : LOW;
  int rstatus = (t == 'F' || t == 'R') ? HIGH : LOW;
 
  digitalWrite(LMOTOR, lstatus);
  digitalWrite(RMOTOR, rstatus);

  delay(DELAYMSEG);
}
