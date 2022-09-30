#define LMOTOR 11
#define RMOTOR 13
#define BAUDRATE 9600
#define DEFAULTCHAR '0'
 
void setup() {
  pinMode(RMOTOR,OUTPUT);   
  pinMode(LMOTOR,OUTPUT);   
  Serial.begin(BAUDRATE);
 
}
 
void loop() {
  char t;
  
  if(Serial.available()){
    t = Serial.read();
    Serial.println(t);
  }
 
  if(t == 'f'){             //move forward(all motors rotate in forward direction)
    digitalWrite(LMOTOR,HIGH);
    digitalWrite(RMOTOR,HIGH);
  } else if(t == 'l'){      //turn right (left side motors rotate in forward direction, right side motors doesn't rotate)
    digitalWrite(LMOTOR,HIGH);
  } else if(t == 'r'){      //turn left (right side motors rotate in forward direction, left side motors doesn't rotate)
    digitalWrite(RMOTOR,HIGH);
  } else {                  //STOP (all motors stop)
    digitalWrite(LMOTOR,LOW);
    digitalWrite(RMOTOR,LOW);
  }
  delay(500);
  t = DEFAULTCHAR;
}
