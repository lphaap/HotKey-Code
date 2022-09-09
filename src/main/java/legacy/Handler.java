package legacy;

public interface Handler {
	
	enum Hotkey {
		ALT_GR_Q, ALT_GR_W, ALT_GR_E, ALT_GR_R, ALT_GR_T,
		ALT_GR_A, ALT_GR_S, ALT_GR_D, ALT_GR_F, ALT_GR_G,
		ALT_GR_Z, ALT_GR_X, ALT_GR_C, ALT_GR_V,
	} 
	
	/*
	 * Handle input for all hot keys
	 */
	public void handleInput(Hotkey key);
}
