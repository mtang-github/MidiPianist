package midi;

/**
 * A collection of constants relevant to low level midi.
 */
@SuppressWarnings("unused")
public class MidiConstants{

    private MidiConstants(){}

    //midi events; last 4 bits are channel #
    public static final int NOTE_OFF =						    0b10000000;
    public static final int NOTE_ON =						    0b10010000;
    public static final int POLYPHONIC_KEY_PRESSURE =			0b10100000;
    public static final int CONTROL_CHANGE =					0b10110000;
    public static final int PROGRAM_CHANGE =					0b11000000;
    public static final int CHANNEL_PRESSURE =				    0b11010000;
    public static final int PITCH_BEND_CHANGE =				    0b11100000;
    public static final int META_EVENT_OR_SYSTEM_EXCLUSIVE =	0b11110000;

    //controller codes
    public static final int BANK_SELECT_MSB =						0;
    public static final int MODULATION_WHEEL_MSB =				    1;
    public static final int BREATH_CONTROLLER_MSB =				    2;
    //undefined							                            //undefined	3
    public static final int FOOT_PEDAL_MSB =						4;
    public static final int PORTAMENTO_TIME_MSB =					5;
    public static final int DATA_ENTRY_MSB =						6;
    public static final int CHANNEL_VOLUME_MSB =					7,  MAIN_VOLUME_MSB =		7;
    public static final int BALANCE_MSB =						    8;
    //undefined							                            //undefined 9
    public static final int PAN_MSB =							    10;
    public static final int EXPRESSION_MSB =						11;
    public static final int EFFECT_CONTROL_1_MSB =					12;
    public static final int EFFECT_CONTROL_2_MSB =					13;
    //undefined							                            //undefined 14
    //undefined							                            //undefined 15
    public static final int GENERAL_PURPOSE_CONTROLLER_1_MSB =		16;
    public static final int GENERAL_PURPOSE_CONTROLLER_2_MSB =		17;
    public static final int GENERAL_PURPOSE_CONTROLLER_3_MSB =		18;
    public static final int GENERAL_PURPOSE_CONTROLLER_4_MSB =		19;
    //undefined							                            //undefined 20
    //.									                            //.
    //.									                            //.
    //.									                            //.
    //undefined							                            //undefined 31
    public static final int BANK_SELECT_LSB =						32;
    public static final int MODULATION_WHEEL_LSB =				    33;
    public static final int BREATH_CONTROLLER_LSB =				    34;
    //undefined							                            //undefined 35
    public static final int FOOT_PEDAL_LSB =						36;
    public static final int PORTAMENTO_TIME_LSB =					37;
    public static final int DATA_ENTRY_LSB =						38;
    public static final int CHANNEL_VOLUME_LSB =					39,  MAIN_VOLUME_LSB =		39;
    public static final int BALANCE_LSB =						    40;
    //undefined							                            //undefined 41
    public static final int PAN_LSB =							    42;
    public static final int EXPRESSION_LSB =						43;
    public static final int EFFECT_CONTROL_1_LSB =					44;
    public static final int EFFECT_CONTROL_2_LSB =					45;
    //undefined							                            //undefined 46,
    //undefined							                            //undefined 47,
    public static final int GENERAL_PURPOSE_CONTROLLER_1_LSB =		48;
    public static final int GENERAL_PURPOSE_CONTROLLER_2_LSB =		49;
    public static final int GENERAL_PURPOSE_CONTROLLER_3_LSB =		50;
    public static final int GENERAL_PURPOSE_CONTROLLER_4_LSB =		51;
    //undefined							                            //undefined 52,
    //.									                            //.
    //.									                            //.
    //.									                            //.
    //undefined							                            //undefined 63,
    public static final int SUSTAIN_SWITCH =						64;
    public static final int PORTAMENTO_SWITCH =					    65;
    public static final int SOSTENUTO_SWITCH =					    66;
    public static final int SOFT_PEDAL_SWITCH =					    67;
    public static final int LEGATO_SWITCH =						    68;
    public static final int HOLD_2_SWITCH =						    69;
    public static final int SOUND_CONTROLLER_1 =					70,  SOUND_VARIATION =		70;
    public static final int SOUND_CONTROLLER_2 =					71,  SOUND_TIMBRE =			71;
    public static final int SOUND_CONTROLLER_3 =					72,  RELEASE_TIME =			72;
    public static final int SOUND_CONTROLLER_4 =					73,  ATTACK_TIME =			73;
    public static final int SOUND_CONTROLLER_5 =					74,	 BRIGHTNESS =			74;
    public static final int SOUND_CONTROLLER_6 =					75,  DECAY_TIME =			75;
    public static final int SOUND_CONTROLLER_7 =					76,  VIBRATO_RATE =			76;
    public static final int SOUND_CONTROLLER_8 =					77,  VIBRATO_DEPTH =			77;
    public static final int SOUND_CONTROLLER_9 =					78,  VIBRATO_DELAY =			78;
    public static final int SOUND_CONTROLLER_0 =					79;
    public static final int GENERAL_PURPOSE_CONTROLLER_5 =			80;
    public static final int GENERAL_PURPOSE_CONTROLLER_6 =			81;
    public static final int GENERAL_PURPOSE_CONTROLLER_7 =			82;
    public static final int GENERAL_PURPOSE_CONTROLLER_8 =			83;
    public static final int PORTAMENTO_CONTROL =					84;
    //undefined							                            //undefined 85
    //undefined							                            //undefined 86
    //undefined							                            //undefined 87
    public static final int HIGH_RESOLUTION_VELOCITY_PREFIX =		88;
    //undefined							                            //undefined 89
    //undefined							                            //undefined 90
    public static final int EFFECTS_1_DEPTH =						91,  REVERB =				91;
    public static final int EFFECTS_2_DEPTH =						92,  TREMELO =				92;
    public static final int EFFECTS_3_DEPTH =						93,  CHORUS =				93;
    public static final int EFFECTS_4_DEPTH =						94,  DETUNE = 94, DELAY =	94;
    public static final int EFFECTS_5_DEPTH =						95,  PHASER =				95;
    public static final int DATA_INCREMENT =						96;
    public static final int DATA_DECREMENT =						97;
    public static final int NON_REGISTERED_PARAMETER_NUMBER_LSB =	98;
    public static final int NON_REGISTERED_PARAMETER_NUMBER_MSB =	99;
    public static final int REGISTERED_PARAMETER_NUMBER_LSB =		100;
    public static final int REGISTERED_PARAMETER_NUMBER_MSB =		101;
    //undefined							                            //undefined 102,
    //.									                            //.
    //.									                            //.
    //.									                            //.
    //undefined							                            //undefined 119,

    //channel mode messages
    public static final int ALL_SOUND_OFF =						    120;
    public static final int RESET_ALL_CONTROLLERS =				    121;
    public static final int LOCAL_CONTROL =						    122;
    public static final int ALL_NOTES_OFF =						    123;
    public static final int OMNI_MODE_OFF =						    124;
    public static final int OMNI_MODE_ON =						    125;
    public static final int MONO_MODE_ON =						    126;
    public static final int POLY_MODE_ON =						    127;
}