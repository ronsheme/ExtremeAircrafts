package akkaLabs.ExtremeAircrafts.commands.aircraft;

public class ModifyAircrafts {
		private int numOfAircrafts;

		public ModifyAircrafts(int numOfAircrafts)
		{
			this.numOfAircrafts = numOfAircrafts;
		}

		public int getNumOfAircrafts()
		{
			return numOfAircrafts;
		}
}
