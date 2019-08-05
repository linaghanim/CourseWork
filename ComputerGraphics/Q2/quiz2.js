
/*
Q2a -- this is the ThreeJS program that you must modify for Quiz 2
It is essentially the same as the practice quiz but we have stripped
out unneeded parts (e.g. skybox, sound effects)

You will need to modify the following functions
  - createMainScene -- to add new elements to the scene
  -
*/


	// First we declare the variables that hold the objects we need
	// in the animation code
	var scene, renderer;  // all threejs programs need these
	var camera, avatarCam, edgeCam;  // we have three cameras in the main scene
	var avatar;
	var blueCylinder,greenBox;
  var cone, ball;


  // here are the controls for the avatar
  // these determine what linear and angular velocities we apply to objects

var controls =
	   {fwd:false, bwd:false, left:false, right:false,speed:10, reset:false}

  // the gameState is used to specify the camera to use
	var gameState = {camera:'none' }


	// Here is the main game control
  initPhysijsScene();
  initRenderer();
  createMainScene();
	initControls();
	animate();  // start the animation loop!


  function initPhysijsScene(){
    Physijs.scripts.worker = 'physijs_worker.js';
    Physijs.scripts.ammo = 'ammo.js';
    scene = new Physijs.Scene();
  }

	function initRenderer(){
		renderer = new THREE.WebGLRenderer();
		renderer.setSize( window.innerWidth, window.innerHeight-50 );
		document.body.appendChild( renderer.domElement );
		renderer.shadowMap.enabled = true;
		renderer.shadowMap.type = THREE.PCFSoftShadowMap;
	}








	function createMainScene(){
      // add a point light and an ambient light
			var light1 = createPointLight();
			light1.position.set(0,200,20);
			scene.add(light1);
			var light0 = new THREE.AmbientLight( 0xffffff,0.25);
			scene.add(light0);

			// create main camera
			camera = new THREE.PerspectiveCamera( 90, window.innerWidth / window.innerHeight, 0.1, 1000 );
			camera.position.set(0,50,0);
			camera.lookAt(0,0,0);


			// create the ground and the skybox
			var ground = createGround('grass.png');
			scene.add(ground);

      // ADD YOUR QUIZ2 CODE TO MAKE THE BOX AND PIN HERE ...

      cone = new Physijs.ConeMesh(
        new THREE.ConeGeometry(6,16,16),
        new THREE.MeshLambertMaterial({color:0x00ffff})
      )
      cone.position.set(-20,8,-20)
      scene.add(cone)

      ball = new Physijs.SphereMesh(
        new THREE.SphereGeometry(6,16,16),
        new THREE.MeshLambertMaterial({color:0xff0000})
      )
      ball.position.set(-20,6,0);
      scene.add(ball)


			// create the avatar and avatarCam
			avatarCam = new THREE.PerspectiveCamera( 60, window.innerWidth / window.innerHeight, 0.1, 1000 );
			avatar = createAvatar();

			scene.add(avatar);
			gameState.camera = avatarCam;

      edgeCam = new THREE.PerspectiveCamera( 90, window.innerWidth / window.innerHeight, 0.1, 1000 );
      edgeCam.position.set(0,100,100);
			gameState.camera = edgeCam;


			 blueCylinder =
			new Physijs.CylinderMesh(
				new THREE.CylinderGeometry(2,2,20,10),
				new THREE.MeshLambertMaterial({color:0x0066ff})
			)
			blueCylinder.position.set(-30,20,20)


			blueCylinder.addEventListener('collision',
				function showEating (other_object)
				{
					if(other_object == ball)
					{
						cone.__dirtyRotation = true
            cone.setLinearVelocity(new THREE.Vector3(0,20,0))
					}


				})
				scene.add(blueCylinder)

			greenBox = createBoxMesh(0x00ff00);
			greenBox.position.set(40,30,20)
			greenBox.rotation.z=Math.PI/5;
			scene.add(greenBox)
	}





	function createPointLight(){
		var light;
		light = new THREE.PointLight( 0xffffff);
		light.castShadow = true;
		//Set up shadow properties for the light
		light.shadow.mapSize.width = 2048;  // default
		light.shadow.mapSize.height = 2048; // default
		light.shadow.camera.near = 0.5;       // default
		light.shadow.camera.far = 500      // default
		return light;
	}



	function createBoxMesh(color){
		var geometry = new THREE.BoxGeometry( 100, 2, 10);
		var material = new THREE.MeshLambertMaterial( { color: color} );
		mesh = new Physijs.BoxMesh( geometry, material );
    mesh = new Physijs.BoxMesh( geometry, material,0 );
		mesh.castShadow = true;
		return mesh;
	}

	function createBoxMesh2(color,w,h,d){
		var geometry = new THREE.BoxGeometry( w, h, d);
		var material = new THREE.MeshLambertMaterial( { color: color} );
		mesh = new Physijs.BoxMesh( geometry, material );
		//mesh = new Physijs.BoxMesh( geometry, material,0 );
		mesh.castShadow = true;
		return mesh;
	}




	function createAvatar(){
		var geometry = new THREE.BoxGeometry( 5, 5, 6);
		var material = new THREE.MeshLambertMaterial( { color: 0xffff00} );
		var pmaterial = new Physijs.createMaterial(material,0.9,0.5);
		var mesh = new Physijs.BoxMesh( geometry, pmaterial );
		mesh.setDamping(0.1,0.1);
		mesh.castShadow = true;

		avatarCam.position.set(0,4,0);
		avatarCam.lookAt(0,4,10);
		mesh.add(avatarCam);

    mesh.translateY(5)
    mesh.translateZ(-10)
    avatarCam.translateY(-4);
    avatarCam.translateZ(3);


		return mesh;
	}


	function createConeMesh(r,h){
		var geometry = new THREE.ConeGeometry( r, h, 32);
		var texture = new THREE.TextureLoader().load( 'grass.jpg' );
		texture.wrapS = THREE.RepeatWrapping;
		texture.wrapT = THREE.RepeatWrapping;
		texture.repeat.set( 1, 1 );
		var material = new THREE.MeshLambertMaterial( { color: 0xffffff,  map: texture ,side:THREE.DoubleSide} );
		var pmaterial = new Physijs.createMaterial(material,0.9,0.5);
		var mesh = new Physijs.ConeMesh( geometry, pmaterial, 0 );
		mesh.castShadow = true;
		return mesh;
	}


	function createBall(){
		//var geometry = new THREE.SphereGeometry( 4, 20, 20);
		var geometry = new THREE.SphereGeometry( 1, 16, 16);
		var material = new THREE.MeshLambertMaterial( { color: 0x444444} );
		var pmaterial = new Physijs.createMaterial(material,0.9,0.95);
    var mesh = new Physijs.BoxMesh( geometry, pmaterial );
		mesh.setDamping(0.1,0.1);
		mesh.castShadow = true;
		return mesh;
	}





	var clock;

	function initControls(){
		// here is where we create the eventListeners to respond to operations

		  //create a clock for the time-based animation ...
			clock = new THREE.Clock();
			clock.start();

			window.addEventListener( 'keydown', keydown);
			window.addEventListener( 'keyup',   keyup );
  }

	function keydown(event){
		console.log("Keydown: '"+event.key+"'");
		//console.dir(event);
		// first we handle the "play again" key in the "youwon" scene

		// this is the regular scene
		switch (event.key){
			// change the way the avatar is moving
			case "w": controls.fwd = true;  break;
			case "s": controls.bwd = true; break;
			case "a": controls.left = true; break;
			case "d": controls.right = true; break;

			case "m": controls.speed = 30; break;

			case "r": {

					ball.__dirtyPosition = true;
					ball.position.set(-20,6,0);
					ball.setLinearVelocity(new THREE.Vector3(0,0,0))
					ball.setAngularVelocity(new THREE.Vector3(0,0,0))


					blueCylinder.position.set(-30,20,20)
					blueCylinder.__dirtyPosition = true;

					blueCylinder.rotation.set(0,0,0)
					blueCylinder.__dirtyRotation = true;

					blueCylinder.setLinearVelocity(new THREE.Vector3(0,0,0))
					blueCylinder.setAngularVelocity(new THREE.Vector3(0,0,0))
			}

			break;

			case "p":{
				ball.position.set(60,70,20);
				ball.__dirtyPosition = true;

			}
			break;

			// switch cameras
			case "1": gameState.camera = camera; break;
			case "2": gameState.camera = avatarCam; break;
      case "3": gameState.camera = edgeCam; break;

		}

	}

	function keyup(event){
		//console.log("Keydown:"+event.key);
		//console.dir(event);
		switch (event.key){
			case "w": controls.fwd   = false;  break;
			case "s": controls.bwd   = false; break;
			case "a": controls.left  = false; break;
			case "d": controls.right = false; break;

			case "m": controls.speed = 10; break;

			//case "r": controls.reset = false; break;
		}
	}



  function updateAvatar(){
		"change the avatar's linear or angular velocity based on controls state (set by WSAD key presses)"

		var forward = avatar.getWorldDirection();

		if (controls.fwd){
			avatar.setLinearVelocity(forward.multiplyScalar(controls.speed));
		} else if (controls.bwd){
			avatar.setLinearVelocity(forward.multiplyScalar(-controls.speed));
		} else {
			var velocity = avatar.getLinearVelocity();
			velocity.x=velocity.z=0;
			avatar.setLinearVelocity(velocity); //stop the xz motion
		}


		if (controls.left){
			avatar.setAngularVelocity(new THREE.Vector3(0,controls.speed*0.1,0));
		} else if (controls.right){
			avatar.setAngularVelocity(new THREE.Vector3(0,-controls.speed*0.1,0));
		}


	}



	function animate() {

		requestAnimationFrame( animate );

		updateAvatar();


    edgeCam.lookAt(avatar.position);
    camera.lookAt(avatar.position);

  	scene.simulate();

		if (gameState.camera!= 'none'){
			renderer.render( scene, gameState.camera );
		}

	}

/*
  You don't need to change anything below this line!
*/





function createGround(image){
  // creating a textured plane which receives shadows
  var geometry = new THREE.PlaneGeometry( 180, 180, 128 );
  var texture = new THREE.TextureLoader().load(image );
  texture.wrapS = THREE.RepeatWrapping;
  texture.wrapT = THREE.RepeatWrapping;
  texture.repeat.set( 15, 15 );
  var material = new THREE.MeshLambertMaterial( { color: 0xffffff,  map: texture ,side:THREE.DoubleSide} );
  var pmaterial = new Physijs.createMaterial(material,0.9,0.05);
  //var mesh = new THREE.Mesh( geometry, material );
  var mesh = new Physijs.BoxMesh( geometry, pmaterial, 0 );

  mesh.receiveShadow = true;

  mesh.rotateX(Math.PI/2);
  return mesh
  // we need to rotate the mesh 90 degrees to make it horizontal not vertical
}
