package awap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class AWAPMain {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		ObjectMapper objectMapper = new ObjectMapper();
		State initState = objectMapper.readValue(reader.readLine(), State.class);
		
		Game game = new ExpandGame(initState);
		Logger.log("Player: " + initState.getNumber().get());
		
		String nextline;
		while ((nextline = reader.readLine()) != null) {
			State state = objectMapper.readValue(nextline, State.class);

			Optional<Move> move = game.updateState(state);
			if (move.isPresent()) {
				System.out.println(move.get());
				//Logger.log(move.get().toString());
			}
		}
	}
}
