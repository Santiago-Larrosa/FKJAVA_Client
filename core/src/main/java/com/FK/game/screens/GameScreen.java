    package com.FK.game.screens;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Screen;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.math.MathUtils;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.utils.Array;
    import com.badlogic.gdx.utils.viewport.StretchViewport;
    import com.badlogic.gdx.utils.viewport.Viewport;
    import com.badlogic.gdx.maps.tiled.TiledMap;
    import com.badlogic.gdx.maps.tiled.TmxMapLoader;
    import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
    import com.badlogic.gdx.maps.MapLayer;
    import com.badlogic.gdx.maps.MapObject;
    import com.badlogic.gdx.maps.objects.RectangleMapObject;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
    import com.badlogic.gdx.graphics.Color;  
    import com.FK.game.animations.*;
    import com.FK.game.core.*;
    import com.FK.game.entities.*;
    import com.FK.game.screens.*;
    import com.FK.game.states.*;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.badlogic.gdx.scenes.scene2d.ui.Skin;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.utils.viewport.ScreenViewport;
    import com.FK.game.sounds.*;
    import com.FK.game.maps.*;
    import com.FK.game.ui.*;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.graphics.g2d.ParticleEffect;
    import java.net.DatagramSocket;
    import java.net.DatagramPacket;
    import java.net.InetAddress;
    import java.net.SocketException;
    import java.net.UnknownHostException;
    import java.util.Map;
    import java.util.HashMap;
    import com.FK.game.network.*;


    public class GameScreen implements Screen {
        private final MainGame game;
        private OrthographicCamera camera;
        private Viewport viewport;
        private SpriteBatch batch;
        private Player myPlayer;    
        private int myPlayerId = -1;
        private Array<BaseHUD> hudElements;
        private BitmapFont hudFont;
        private Texture coinTexture;
        private boolean isCameraMoving = false;
        private float cameraMoveStartX, cameraMoveStartY;
        private float cameraMoveTargetX, cameraMoveTargetY;
        private float cameraMoveProgress = 0f;
        private final float CAMERA_TRANSITION_DURATION = 0.8f;
        private static final float WORLD_WIDTH = 800;
        private static final float WORLD_HEIGHT = 480;
        private float cameraOffsetX = 0;
        private float cameraOffsetY = 0;
        private final float CAMERA_MOVE_SPEED = 8f; 
        private float shakeDuration = 0f;
        private float shakeIntensity = 0f;
        private float shakeTime = 0f;
        private float originalCamX, originalCamY;
        private TiledMap map;
        private OrthogonalTiledMapRenderer mapRenderer;
        private Array<Rectangle> collisionObjects = new Array<Rectangle>();
        private ShapeRenderer shapeRenderer;
        private Array<Enemy> enemies; //-
        private Array<Entity> entities;
        private Rectangle playerSpawnPoint; 
        private final Color DarkViolet = new Color(0.1f, 0.05f, 0.15f, 1f); 
        private final Color LightViolet = new Color(0.25f, 0.15f, 0.3f, 1f);    
        private Rectangle portalSpawnPoint; 
        private Portal portal;
        DatagramSocket socket = null;
InetAddress serverAddress = null;
        int serverPort = 54555;
        private final Map<Integer, Player> remotePlayers = new HashMap<>();
        private Texture whitePixelTexture;
        private TextureRegion whitePixelRegion; 
        private Array<ParticleEffect> activeEffects;
        private ParticleEffect groundImpactEffectTemplate;
        private boolean isFirstRun = true;
        private enum GameState {
            RUNNING,
            PAUSED
        }
        private GameState currentState = GameState.RUNNING;
        private Stage uiStage;
        private Skin uiSkin;
        private UpgradeWindow upgradeWindow;
        
        private UpgradeManager upgradeManager;
        public GameScreen(MainGame game) {
            this.game = game;
        }
        // Señal enviada desde ClientThread
private boolean pendingLevelChange = false;
private float pendingLevelChangeTimer = 0f;

        

    @Override
        public void show() {
            shapeRenderer = new ShapeRenderer();
            upgradeManager = new UpgradeManager();
            batch = new SpriteBatch();
            try {
                socket = new DatagramSocket();
                serverAddress = InetAddress.getByName("127.0.0.1"); // o la IP del servidor
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
            if (isFirstRun) {
                isFirstRun = false;
            entities = new Array<>();
            enemies = new Array<>();
            activeEffects = new Array<>();
            GameContext.setScreen(this);
            if (game != null && game.client != null) {
                game.client.notifyScreenReady(this);
            }
            camera = new OrthographicCamera();
            viewport = new StretchViewport(WORLD_WIDTH * 0.7f, WORLD_HEIGHT * 0.7f, camera);
            viewport.apply();
            camera.position.set(WORLD_WIDTH/2 * 0.7f, WORLD_HEIGHT/2 * 0.7f, 0);
            whitePixelTexture = new Texture("white_pixel.jpg");//-
            whitePixelRegion = new TextureRegion(whitePixelTexture);
            if (!AnimationCache.getInstance().update()) {
                game.setScreen(new LoadingScreen(game));
                return;
            }
            groundImpactEffectTemplate = new ParticleEffect();
            groundImpactEffectTemplate.load(Gdx.files.internal("ground_impact.p"), Gdx.files.internal(""));
            uiSkin = new Skin(Gdx.files.internal("ui/glassy-ui.json"));
            uiStage = new Stage(new ScreenViewport());
            hudFont = new BitmapFont();
            coinTexture = new Texture("coin.png");//-
            loadInitialMap(); 
            }
            NetworkInputAdapter networkAdapter = new NetworkInputAdapter(
                game.client,
                Input.Keys.A,        // keyLeft
                Input.Keys.D,        // keyRight
                Input.Keys.W,        // keyJump
                Input.Keys.X,        // keyAttack
                Input.Keys.Z,        // keyFireAttack
                Input.Keys.S         // keyDown
            );
            Gdx.input.setInputProcessor(networkAdapter);
            
            
        }
        public void updatePlayerState(int id, float x, float y, String stateName, String facingName) {
            if (myPlayerId == -1 || myPlayer == null) {
              return;
            }

            if (id == myPlayerId) {
                myPlayer.setTargetPosition(x, y);
                myPlayer.setVisualStateFromServer(stateName, facingName);
                return;
            }

            Player playerToUpdate = remotePlayers.get(id);
            if (playerToUpdate == null) {
                System.out.println("Detectado nuevo jugador remoto con ID: " + id);
                playerToUpdate = new Player(game, new PlayerData()); 
                playerToUpdate.setCurrentAnimation(PlayerAnimationType.IDLE_RIGHT);
                playerToUpdate.setPosition(0, 0);
                playerToUpdate.setCollisionObjects(collisionObjects);
                GameContext.addPlayer(playerToUpdate);
                remotePlayers.put(id, playerToUpdate);
                entities.add(playerToUpdate); 
                System.out.println("REMOTE LIST SIZE=" + remotePlayers.size());
                System.out.println("ENTITY LIST SIZE=" + entities.size);
            }
            
            //System.out.println("Actualizando estado del jugador ID=" + id + " a posición (" + x + ", " + y + ")");
            playerToUpdate.setTargetPosition(x, y);
            playerToUpdate.setVisualStateFromServer(stateName, facingName);
        }

        public int getMyPlayerId() {
            return myPlayerId;
        }

        private void loadInitialMap() {
            game.roomsClearedCount = 0;
            cleanUpCurrentMap();
            
            MapManager mapManager = new MapManager(0.7f);
            mapManager.loadMaps("maps/SpawnHall.tmx");
            map = mapManager.getMaps().first();
            mapRenderer = new OrthogonalTiledMapRenderer(map, mapManager.getScale());
            
            loadCollisionObjects(mapManager.getScale());
            
            Array<Rectangle> portalSpawns = loadSpawnPoints("Portal", mapManager.getScale());
            if (portalSpawns.size > 0) {
                this.portalSpawnPoint = portalSpawns.first();
            }
            loadEntities(mapManager.getScale(), null, true);
        }



        public void setPlayerId(int id) {
            this.myPlayerId = id;
            System.out.println("El ID del jugador se ha establecido en: " + id + "Segunda comprobación");
        }

        public void loadSpecificMap(String mapName) {
            FireAttackHUD existingHUD = myPlayer != null ? myPlayer.getFireAttackHUD() : null;

            cleanUpCurrentMap();

            MapManager mapManager = new MapManager(0.7f);
            Array<String> commonRooms = new Array<>();
            mapManager.loadMaps(mapName);
            map = mapManager.getCurrentMap();
            mapRenderer = new OrthogonalTiledMapRenderer(map, mapManager.getScale());

            loadCollisionObjects(mapManager.getScale());

            Array<Rectangle> portalSpawns = loadSpawnPoints("Portal", mapManager.getScale());
            if (portalSpawns.size > 0) {
                this.portalSpawnPoint = portalSpawns.first();
            }
            System.out.println("cantidad de jugadores remotos antes de cargar el mapa: " + remotePlayers.size());
            System.out.println("La cantidad de entidades antes de cargar el mapa es: " + entities.size);

            loadEntities(mapManager.getScale(), existingHUD, false);
        }

        private void cleanUpCurrentMap() {
            if (map != null) map.dispose();
            if (mapRenderer != null) mapRenderer.dispose();
            collisionObjects.clear();
            entities.clear();
            enemies.clear();
            remotePlayers.clear();
            portal = null;
            portalSpawnPoint = null;
        }


    private void openUpgradeMenu() {
            if (myPlayer != null) {
            myPlayer.getStateMachine().changeState(new IdleState());
        }

            upgradeWindow = new UpgradeWindow(uiSkin, this::closeUpgradeMenu, game.playerData, upgradeManager);
            uiStage.addActor(upgradeWindow);
            upgradeWindow.centerWindow();
            Gdx.input.setInputProcessor(uiStage);
        }

        private void closeUpgradeMenu() {
            currentState = GameState.RUNNING;
            upgradeWindow.remove(); 
            Gdx.input.setInputProcessor(null); 
            Gdx.app.log("GameScreen", "Menú de mejoras CERRADO. Juego reanudado.");
        }
        private void loadEntities(float scale, FireAttackHUD existingHUD, boolean isSpawnHall) {
            Array<Rectangle> playerSpawns = loadSpawnPoints("Player", scale);
            Array<Rectangle> bolbSpawns = loadSpawnPoints("Bolb", scale);
            Array<Rectangle> slopSpawns = loadSpawnPoints("Slop", scale);
            Array<Rectangle> fungopSpawns = loadSpawnPoints("Fungop", scale);
            Array<Rectangle> fireSpawns = loadSpawnPoints("Fire", scale);
            Array<Rectangle> bossSpawns = loadSpawnPoints("Boss", scale);

            if (playerSpawns.size > 0) {
                Rectangle spawn = playerSpawns.first();
                playerSpawnPoint = spawn;
                
                myPlayer = new Player(game, game.playerData);
                myPlayer.setCurrentAnimation(PlayerAnimationType.IDLE_RIGHT);
                myPlayer.setPosition(spawn.x, spawn.y);
                myPlayer.setCollisionObjects(collisionObjects);
                hudElements = new Array<>();

                
                entities.add(myPlayer);
                GameContext.setPlayer(myPlayer);

                FireAttackHUD fireHUD = new FireAttackHUD();
                FireAttackHUD fireHUD2 = new FireAttackHUD();
                myPlayer.setFireAttackHUD(fireHUD); 
                hudElements.add(fireHUD);

                CoinHUD coinHUD1 = new CoinHUD(game.playerData, coinTexture, hudFont);
                hudElements.add(coinHUD1);
                GameContext.addPlayer(myPlayer); 
            }

            for (Rectangle spawn : bolbSpawns) {
                Enemy bolb = new Bolb(collisionObjects);
                bolb.setPosition(spawn.x, spawn.y);
                enemies.add(bolb);
                entities.add(bolb);
            }

            for (Rectangle spawn : slopSpawns) {
                Enemy slop = new Slop(collisionObjects);
                slop.setPosition(spawn.x, spawn.y);
                enemies.add(slop);
                entities.add(slop);
            }

            for (Rectangle spawn : fungopSpawns) {
                Enemy fungop = new Fungop(collisionObjects);
                fungop.setPosition(spawn.x, spawn.y);
                enemies.add(fungop);
                entities.add(fungop);
            }

            for (Rectangle spawn : bossSpawns) {
                Boss boss = new Boss(collisionObjects);
                boss.setPosition(spawn.x, spawn.y);
                enemies.add(boss);
                entities.add(boss);
            }

            for (Rectangle spawn : fireSpawns) {
            Fire fire = new Fire(spawn.x, spawn.y);
            entities.add(fire);
        }
        }


        private void loadCollisionObjects(float scale) {
            MapLayer collisionLayer = map.getLayers().get("Capa de Objetos 1");
            
            if (collisionLayer != null) {
                for (MapObject object : collisionLayer.getObjects()) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();
                        rect.x *= scale;
                        rect.y *= scale;
                        rect.width *= scale;
                        rect.height *= scale;
                        collisionObjects.add(rect);
                    }
                }
            } else {
                Gdx.app.log("DEBUG", "No se encontró la capa de colisiones");
            }
        }
        public void notifyLevelChange() {
    System.out.println("[GameScreen] notifyLevelChange() llamado — cambio de nivel pendiente");
    pendingLevelChange = true;
    pendingLevelChangeTimer = 0f; // opcional
}


private void updateEntities(float delta) {
        if (myPlayer != null) {
            SoundCache.getInstance().updateSpatialLoops(myPlayer);
        }

        for (int i = entities.size - 1; i >= 0; i--) {
            
            Entity e = entities.get(i);
            if (e instanceof Player) {
                Player player = (Player) e;
                if (player == myPlayer) {
                    player.updateFireCooldown(delta);
                }
                
            }
            if (e instanceof CharacterEntity<?>) {
                ((CharacterEntity<?>) e).updateDamageCooldown(delta); 
            }
            if (e instanceof Entity && ((Entity) e).isReadyForRemoval()) {
                
                if (e == myPlayer) {
                    game.playerData.resetOnDeath();
                    game.setScreen(new GameScreen(game)); 
                }
                
                entities.removeIndex(i);
                if (e instanceof Enemy) {
                    enemies.removeValue((Enemy) e, true);
                }
                continue; 
            }
            e.update(delta);
            if (e instanceof Coin) {
                Coin coin = (Coin) e;
                if (coin.getTarget() == null) {
                    Player closestPlayer = null;
                    float minDistance = Float.MAX_VALUE;
                    Vector2 coinCenter = coin.getCenter();
                    
                    if (myPlayer != null && !myPlayer.isDead()) {
                        float dist = myPlayer.getCenter().dst(coinCenter);
                        minDistance = dist;
                        closestPlayer = myPlayer;
                    }

                    for (Player remotePlayer : remotePlayers.values()) {
                        if (remotePlayer != null && !remotePlayer.isDead()) {
                            float dist = remotePlayer.getCenter().dst(coinCenter);
                            if (dist < minDistance) {
                                minDistance = dist;
                                closestPlayer = remotePlayer;
                            }
                        }
                    }
                    coin.setTarget(closestPlayer);
                }
            }
        } 
        for (BaseHUD hud : hudElements) {
            hud.update(delta);
        }
    }

        private void checkEntityDamage() {
            for (int i = 0; i < entities.size; i++) {
                Entity attacker = entities.get(i);
                Rectangle damageBox = attacker.getDamageBox();

                if (damageBox.width == 0 || damageBox.height == 0) continue;

                for (int j = 0; j < entities.size; j++) {
                    if (i == j) continue;

                    Entity target = entities.get(j);

                    if (damageBox.overlaps(target.getCollisionBox())) {                    
                        if (attacker instanceof Player && target instanceof Fire ) {
                            openUpgradeMenu();
                        } else {
                            if (target instanceof CharacterEntity) {
                                ((CharacterEntity) target).receiveDamage(attacker);
                            }
                        }
                    }
                }
            }
        }

        private Array<Rectangle> loadSpawnPoints(String layerName, float scale) {
        Array<Rectangle> spawnPoints = new Array<>();
        MapLayer layer = map.getLayers().get(layerName);

        if (layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    rect.x *= scale;
                    rect.y *= scale;
                    rect.width *= scale;
                    rect.height *= scale;
                    spawnPoints.add(rect);
                }
            }
        } else {
            Gdx.app.log("DEBUG", "No se encontró la capa: " + layerName);
        }
        return spawnPoints;
    }

    public void createImpactEffect(float x, float y) {
            if (groundImpactEffectTemplate == null) return;
            ParticleEffect effect = new ParticleEffect(groundImpactEffectTemplate);
            effect.setPosition(x, y);
            effect.start();
            activeEffects.add(effect);
        }

    @Override
        public void render(float delta) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                if (currentState == GameState.RUNNING) {
                    openUpgradeMenu();
                } else {
                    closeUpgradeMenu();
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MenuScreen(game));
                return;
            }

            if (myPlayerId == -1) { // ¿Todavía no sé quién soy?
                int id = game.client.getPlayerId(); // Pregúntale al hilo de red
                
                if (id != -1) { // ¿El hilo ya tiene un ID?
                    System.out.println("[GameScreen] ¡Mi ID ha sido asignado: " + id + "!");
                    this.myPlayerId = id;
                }
            }
            if (pendingLevelChange) {
            // OPCIONAL: asegurarse unos frames si querés debuggear
            pendingLevelChangeTimer += delta;
            if (pendingLevelChangeTimer > 0.05f) { // 50ms
                pendingLevelChange = false;

                System.out.println("[GameScreen] Aplicando cambio de nivel…");
                game.setScreen(new InterlevelLoadingScreen(game, this));
                return;
            }
            }



            updateEntities(delta);
            updateCamera(delta);
            
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            shapeRenderer.setProjectionMatrix(uiStage.getCamera().combined);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                LightViolet, LightViolet, DarkViolet, DarkViolet);
            
            shapeRenderer.end();
            mapRenderer.setView(camera);
            mapRenderer.render();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            {
                for (Entity e : entities) {
       
                    e.render(batch);
                }
                for (int i = activeEffects.size - 1; i >= 0; i--) {
                    ParticleEffect effect = activeEffects.get(i);
                    effect.update(delta);
                    effect.draw(batch);   
                    if (effect.isComplete()) {
                        effect.dispose();
                        activeEffects.removeIndex(i);
                    }
                }
                for (BaseHUD hud : hudElements) {
                    hud.render(batch, camera);
                }
            }

            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND); 
            
            for (Entity e : entities) {
                if (e instanceof Boss) {
                    Boss boss = (Boss) e;
                    if (boss.getStateMachine().getCurrentState() instanceof BossLaserAttackState) {
                        BossLaserAttackState state = (BossLaserAttackState) boss.getStateMachine().getCurrentState();
                        shapeRenderer.setProjectionMatrix(camera.combined);
                        shapeRenderer.identity();
                        Vector2 bossCenter = new Vector2(boss.getX() + boss.getWidth()/2, boss.getY() + boss.getHeight()/2);
                        shapeRenderer.translate(bossCenter.x, bossCenter.y, 0);
                        shapeRenderer.rotate(0, 0, 1, state.getAttackAngle());
                        state.renderWarning(shapeRenderer);
                    }
                }
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
            
            batch.begin();
            for (Entity e : entities) {
                if (e instanceof Boss) {
                    Boss boss = (Boss) e;
                    if (boss.getStateMachine().getCurrentState() instanceof BossLaserAttackState) {
                        BossLaserAttackState state = (BossLaserAttackState) boss.getStateMachine().getCurrentState();
                        state.renderBeam(batch, whitePixelRegion, boss);
                    }
                }
            }
            batch.end();

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.identity();
            for (Entity e: this.entities) {
                e.renderDebug(shapeRenderer);
                e.renderDebugDamage(shapeRenderer);
            }
            uiStage.act(Math.min(delta, 1 / 30f));
            uiStage.draw();
            
        }


    
        

            
        private void updateCamera(float delta) {
            float playerCenterX = myPlayer.getBounds().x + myPlayer.getBounds().width / 2f;
            float playerCenterY = myPlayer.getBounds().y + myPlayer.getBounds().height / 2f;
            float offsetX = myPlayer.isMovingRight() ? 100f : -100f; 
            float offsetY = 20f; 
            float targetX = playerCenterX + offsetX;
            float targetY = playerCenterY + offsetY;
            float lerpSpeed = 3f; 
            camera.position.x += (targetX - camera.position.x) * lerpSpeed * delta;
            camera.position.y += (targetY - camera.position.y) * lerpSpeed * delta;
            if (shakeTime < shakeDuration) {
                shakeTime += delta;
                float currentIntensity = shakeIntensity * (1 - (shakeTime / shakeDuration));
                float shakeX = MathUtils.random(-1f, 1f) * currentIntensity;
                float shakeY = MathUtils.random(-1f, 1f) * currentIntensity;
                camera.position.x += shakeX;
                camera.position.y += shakeY;
            } else if (shakeDuration > 0f) {
                shakeDuration = 0f;
            }

            camera.update();
        }

        public void shakeCamera(float duration, float intensity) {
            this.shakeDuration = duration;
            this.shakeIntensity = intensity;
            this.shakeTime = 0f;
            this.originalCamX = camera.position.x;
            this.originalCamY = camera.position.y;
        }

        @Override
        public void resize(int width, int height) {
            viewport.update(width, height);
            uiStage.getViewport().update(width, height, true);
        }

        @Override
        public void pause() {}

        @Override
        public void resume() {}

        @Override
        public void hide() {}

        @Override
        public void dispose() {
            batch.dispose();
            for (Entity e : entities) {
                e.dispose();
            }   
            map.dispose();
            mapRenderer.dispose();
            collisionObjects.clear();
            if (groundImpactEffectTemplate != null) {
                groundImpactEffectTemplate.dispose();
            }
            for (ParticleEffect effect : activeEffects) {
                effect.dispose();
            }
            hudFont.dispose();
            coinTexture.dispose();
            uiStage.dispose();
            whitePixelTexture.dispose();
            uiSkin.dispose();
        }
    }
