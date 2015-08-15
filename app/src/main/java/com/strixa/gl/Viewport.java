package com.strixa.gl;

import android.opengl.Matrix;

import junit.framework.Assert;

import java.util.Stack;

public class Viewport
{
    public static class Camera
    {
        private float[] direction = new float[4];
        private float[] matrix = new float[16];
        private float[] position = new float[3];
        private float[] rotation = new float[3];
        private float[] up = new float[4];


        public Camera()
        {
            this.setPosition(0, 0, 0);
            this.setRotation(0, 0, 0);
        }

        public void adjustPosition(float[] position)
        {
            Assert.assertTrue(position != null);
            Assert.assertTrue(position.length == 3);


            this.adjustPosition(position[0], position[1], position[2]);
        }

        public void adjustPosition(float x, float y, float z)
        {
            this.position[0] += x;
            this.position[1] += y;
            this.position[2] += z;

            this.update();
        }

        public float[] getDirection()
        {
            return this.direction;
        }

        public float[] getMatrix()
        {
            return this.matrix;
        }

        public float[] getPosition()
        {
            return this.position;
        }

        public float[] getRotation()
        {
            return this.rotation;
        }

        public void rotateHorizontal(float degrees)
        {
            float yaw;


            yaw = (float)Math.toRadians(degrees);
            if(yaw != 0)
            {
                matrix = new float[16];
                matrix[0] = (float)(Math.cos(yaw) + this.up[0] * this.up[0] * (1 - Math.cos(yaw)));
                matrix[1] = (float)(this.up[0] * this.up[1] * (1 - Math.cos(yaw)) - this.up[2] * Math.sin(yaw));
                matrix[2] = (float)(this.up[0] * this.up[2] * (1 - Math.cos(yaw)) + this.up[1] * Math.sin(yaw));
                matrix[3] = 0;
                matrix[4] = (float)(this.up[0] * this.up[1] * (1 - Math.cos(yaw)) + this.up[2] * Math.sin(yaw));
                matrix[5] = (float)(Math.cos(yaw) + this.up[1] * this.up[1] * (1 - Math.cos(yaw)));
                matrix[6] = (float)(this.up[1] * this.up[2] * (1 - Math.cos(yaw)) - this.up[0] * Math.sin(yaw));
                matrix[7] = 0;
                matrix[8] = (float)(this.up[0] * this.up[2] * (1 - Math.cos(yaw)) - this.up[1] * Math.sin(yaw));
                matrix[9] = (float)(this.up[1] * this.up[2] * (1 - Math.cos(yaw)) + this.up[0] * Math.sin(yaw));
                matrix[10] = (float)(Math.cos(yaw) + this.up[2] * this.up[2] * (1 - Math.cos(yaw)));
                matrix[11] = 0;
                matrix[12] = 0;
                matrix[13] = 0;
                matrix[14] = 0;
                matrix[15] = 1;

                Matrix.multiplyMV(this.direction,0,matrix,0,this.direction,0);

                this.update();
            }
        }

        public void rotateTilt(float degrees)
        {
            float tilt;


            tilt = (float)Math.toRadians(degrees);
            if(tilt != 0)
            {
                matrix = new float[16];
                matrix[0] = (float)(Math.cos(tilt) + this.direction[0] * this.direction[0] * (1 - Math.cos(tilt)));
                matrix[1] = (float)(this.direction[0] * this.direction[1] * (1 - Math.cos(tilt)) - this.direction[2] * Math.sin(tilt));
                matrix[2] = (float)(this.direction[0] * this.direction[2] * (1 - Math.cos(tilt)) + this.direction[1] * Math.sin(tilt));
                matrix[3] = 0;
                matrix[4] = (float)(this.direction[0] * this.direction[1] * (1 - Math.cos(tilt)) + this.direction[2] * Math.sin(tilt));
                matrix[5] = (float)(Math.cos(tilt) + this.direction[1] * this.direction[1] * (1 - Math.cos(tilt)));
                matrix[6] = (float)(this.direction[1] * this.direction[2] * (1 - Math.cos(tilt)) - this.direction[0] * Math.sin(tilt));
                matrix[7] = 0;
                matrix[8] = (float)(this.direction[0] * this.direction[2] * (1 - Math.cos(tilt)) - this.direction[1] * Math.sin(tilt));
                matrix[9] = (float)(this.direction[1] * this.direction[2] * (1 - Math.cos(tilt)) + this.direction[0] * Math.sin(tilt));
                matrix[10] = (float)(Math.cos(tilt) + this.direction[2] * this.direction[2] * (1 - Math.cos(tilt)));
                matrix[11] = 0;
                matrix[12] = 0;
                matrix[13] = 0;
                matrix[14] = 0;
                matrix[15] = 1;

                Matrix.multiplyMV(this.up,0,matrix,0,this.up,0);

                this.update();
            }
        }

        public void rotateVertical(float degrees)
        {
            float[] matrix;
            float[] right;
            float pitch;


            pitch = (float)Math.toRadians(degrees);
            if(pitch != 0)
            {
                /* Obtain the right vector by taking the cross product of the up and direction vectors */
                right = new float[3];
                right[0] = -(this.direction[1] * this.up[2] - this.direction[2] * this.up[1]);
                right[1] = -(this.direction[2] * this.up[0] - this.direction[0] * this.up[2]);
                right[2] = -(this.direction[0] * this.up[1] - this.direction[1] * this.up[0]);

                matrix = new float[16];
                matrix[0] = (float)(Math.cos(pitch) + right[0] * right[0] * (1 - Math.cos(pitch)));
                matrix[1] = (float)(right[0] * right[1] * (1 - Math.cos(pitch)) - right[2] * Math.sin(pitch));
                matrix[2] = (float)(right[0] * right[2] * (1 - Math.cos(pitch)) + right[1] * Math.sin(pitch));
                matrix[3] = 0;
                matrix[4] = (float)(right[0] * right[1] * (1 - Math.cos(pitch)) + right[2] * Math.sin(pitch));
                matrix[5] = (float)(Math.cos(pitch) + right[1] * right[1] * (1 - Math.cos(pitch)));
                matrix[6] = (float)(right[1] * right[2] * (1 - Math.cos(pitch)) - right[0] * Math.sin(pitch));
                matrix[7] = 0;
                matrix[8] = (float)(right[0] * right[2] * (1 - Math.cos(pitch)) - right[1] * Math.sin(pitch));
                matrix[9] = (float)(right[1] * right[2] * (1 - Math.cos(pitch)) + right[0] * Math.sin(pitch));
                matrix[10] = (float)(Math.cos(pitch) + right[2] * right[2] * (1 - Math.cos(pitch)));
                matrix[11] = 0;
                matrix[12] = 0;
                matrix[13] = 0;
                matrix[14] = 0;
                matrix[15] = 1;

                Matrix.multiplyMV(this.direction,0,matrix,0,this.direction,0);
                Matrix.multiplyMV(this.up,0,matrix,0,this.up,0);

                this.update();
            }
        }

        public void setDirection(float x,float y,float z)
        {
            float length;
            float pitch;
            float yaw;


            length = Matrix.length(x,y,z);

            if(z == 0)
            {
                yaw = 0;
            }
            else
            {
                if(z < 0)  //In other words, if we're looking at a point behind us...
                {
                    yaw = 180;
                }
                else
                {
                    yaw = 0;
                }
                z *= -1;  //The Z value must be multiplied by -1 to take the Z-Axis pointing out of the screen into account.

                yaw += (float)Math.toDegrees(Math.atan((double)(x/z)));
            }

            pitch = (float)Math.toDegrees(Math.asin((double)(y/length)));

            this.setRotation(yaw,pitch,this.rotation[2]);
        }

        public void setFocus(float x,float y,float z)
        {
            this.setDirection(x - this.position[0],y - this.position[1],z - this.position[2]);
        }

        public void setPosition(float[] position)
        {
            Assert.assertTrue(position != null);
            Assert.assertTrue(position.length == 3);


            this.setPosition(position[0], position[1], position[2]);
        }

        public void setPosition(float x,float y,float z)
        {
            this.position[0] = x;
            this.position[1] = y;
            this.position[2] = z;

            this.update();
        }

        public void setRotation(float[] rotation)
        {
            Assert.assertTrue(rotation != null);
            Assert.assertTrue(rotation.length == 3);


            this.setRotation(rotation[0], rotation[1], rotation[2]);
        }

        public void setRotation(float horizontal,float vertical,float tilt)
        {
            this.rotation[0] = 0;
            this.rotation[1] = 0;
            this.rotation[2] = 0;

            this.direction[0] = 0;
            this.direction[1] = 0;
            this.direction[2] = 1;
            this.direction[3] = 1;

            this.up[0] = 0;
            this.up[1] = 1;
            this.up[2] = 0;
            this.up[3] = 1;

            this.rotateHorizontal(horizontal);
            this.rotateVertical(vertical);
            this.rotateTilt(tilt);

            this.update();
        }

        private void update()
        {
            Matrix.setLookAtM(
                this.matrix,0,
                this.position[0],this.position[1],this.position[2],
                this.position[0] + this.direction[0],this.position[1] + this.direction[1],this.position[2] + this.direction[2],
                this.up[0],this.up[1],this.up[2]
            );
        }
    }

    public static class ModelMatrix
    {
        private float[] matrix = new float[16];
        private Stack<float[]> state_stack = new Stack<>();


        public ModelMatrix()
        {
            Matrix.setIdentityM(matrix,0);
        }

        public float[] getMatrix()
        {
            return this.matrix;
        }

        public void popState()
        {
            if(this.state_stack.empty())
            {
                Matrix.setIdentityM(this.matrix,0);
            }
            else
            {
                this.matrix = this.state_stack.pop();
            }
        }

        public void pushState()
        {
            this.state_stack.push(this.matrix);
        }

        public void rotateAroundXAxis(float degrees)
        {
            Matrix.rotateM(this.matrix, 0, degrees, 1, 0, 0);
        }

        public void rotateAroundYAxis(float degrees)
        {
            Matrix.rotateM(this.matrix, 0, degrees, 0, 1, 0);
        }

        public void rotateAroundZAxis(float degrees)
        {
            Matrix.rotateM(this.matrix, 0, degrees, 0, 0, 1);
        }

        public void scale(float percent)
        {
            this.scale(percent, percent, percent);
        }

        public void scale(float percent_x,float percent_y,float percent_z)
        {
            Matrix.scaleM(this.matrix, 0, percent_x, percent_y, percent_z);
        }

        public void translate(float dx,float dy,float dz)
        {
            Matrix.translateM(this.matrix,0,dx,dy,dz);
        }
    }


    private float          aspect;
    private Camera         camera;
    private float far_plane_distance;
    private float          height;
    private float near_plane_distance;
    private float          width;


    public Viewport()
    {
        this.setNearPlaneDistance(1);
        this.setFarPlaneDistance(1000);
        this.setDimensions(0,0);
        this.setCamera(new Camera());
    }

    public float[] calculateMVPMatrix(ModelMatrix... matricies)
    {
        return this.calculateMVPMatrix(this.getCamera(),matricies);
    }

    public float[] calculateMVPMatrix(Camera camera,ModelMatrix... matricies)
    {
        float mvp_matrix[];


        mvp_matrix = new float[16];
        Matrix.multiplyMM(mvp_matrix,0,this.getProjectionMatrix(),0,camera.getMatrix(),0);
        for(ModelMatrix matrix : matricies)
        {
            Matrix.multiplyMM(mvp_matrix,0,mvp_matrix,0,matrix.getMatrix(),0);
        }

        return mvp_matrix;
    }

    public Camera getCamera()
    {
        return this.camera;
    }

    public float getFarPlaneDistance()
    {
        return this.far_plane_distance;
    }

    public float getHeight()
    {
        return this.height;
    }

    public float getNearPlaneDistance()
    {
        return this.near_plane_distance;
    }

    public float[] getProjectionMatrix()
    {
        float matrix[];


        matrix = new float[16];
        Matrix.frustumM(matrix, 0, -this.aspect, this.aspect, -1, 1, this.near_plane_distance, this.far_plane_distance);

        return matrix;
    }

    public float getWidth()
    {
        return this.width;
    }

    public void setCamera(Camera camera)
    {
        Assert.assertTrue(camera != null);


        this.camera = camera;
    }

    public void setDimensions(float width,float height)
    {
        if(height == 0)
        {
            this.aspect = 1;
        }
        else
        {
            this.aspect = width/height;
        }

        this.width = width;
        this.height = height;
    }

    public void setFarPlaneDistance(float distance)
    {
        assert(distance > this.near_plane_distance);


        this.far_plane_distance = distance;
    }

    public void setNearPlaneDistance(float distance)
    {
        assert(distance > 0 && distance < this.far_plane_distance);


        this.near_plane_distance = distance;
    }
}
