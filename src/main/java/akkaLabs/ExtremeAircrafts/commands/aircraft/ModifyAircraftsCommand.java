package akkaLabs.ExtremeAircrafts.commands.aircraft;

public class ModifyAircraftsCommand {
		private int numOfAircrafts;

		public ModifyAircraftsCommand(int numOfAircrafts)
		{
			this.numOfAircrafts = numOfAircrafts;
		}

		public int getNumOfAircrafts()
		{
			return numOfAircrafts;
		}
}
