package com.shatteredpixel.shatteredpixeldungeon.update;


import com.badlogic.gdx.Net;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

import java.util.regex.Pattern;

public class GDUpdateServerice extends UpdateService {

	private static Pattern descPattern = Pattern.compile("(.*?)(\r\n|\n|\r)(\r\n|\n|\r)---", Pattern.DOTALL + Pattern.MULTILINE);
	private static Pattern versionCodePattern = Pattern.compile("internal version number: ([0-9]*)", Pattern.CASE_INSENSITIVE);

	@Override
	public boolean supportsUpdatePrompts() {
		return true;
	}

	@Override
	public boolean supportsBetaChannel() {
		return true;
	}

	@Override
	public void checkForUpdate(boolean useMetered, boolean includeBetas, UpdateResultCallback callback) {

		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()) {
			callback.onConnectionFailed();
			return;
		}

		UpdateChecker.refreshConfig(new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				if (UpdateChecker.config == null) {
					callback.onConnectionFailed();
				} else {
					String latestVersionCode = UpdateChecker.config.get("GRPDVersionCode").asText();
					if (GirlsFrontlinePixelDungeon.versionCode < Integer.parseInt(latestVersionCode)) {
						AvailableUpdateData update = new AvailableUpdateData();
						update.versionName = UpdateChecker.config.get("GRPDGameVersion").asText();
						update.desc = UpdateChecker.config.get("changeLog").asText();

						if(DeviceCompat.isDesktop()){
							update.desc += "\n\n"+Messages.get(TitleScene.class,"you_ues_pc");
						}

						update.URL1 = UpdateChecker.config.get("DownloadLink1").asText();
						update.URL3 = UpdateChecker.config.get("DownloadLink3").asText();
						update.URL2 = UpdateChecker.config.get("Join2").asText();
						update.URL4 = UpdateChecker.config.get("Join").asText();
						callback.onUpdateAvailable(update);
					} else {
						callback.onNoUpdateFound();
					}
				}
			}

			@Override
			public void failed(Throwable t) {
				callback.onConnectionFailed();
			}

			@Override
			public void cancelled() {
				callback.onConnectionFailed();
			}
		});

	}

	@Override
	public void initializeUpdate(AvailableUpdateData update) {
		Game.platform.openURI(update.URL1);
	}

	@Override
	public boolean supportsReviews() {
		return false;
	}

	@Override
	public void initializeReview(ReviewResultCallback callback) {
		//does nothing, no review functionality here
		callback.onComplete();
	}

	@Override
	public void openReviewURI() {
		//does nothing
	}

	private static int[] splitVersion(String version) {
		String[] parts = version.split("\\.");
		int[] numbers = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			numbers[i] = Integer.parseInt(parts[i]);
		}
		return numbers;
	}
}
