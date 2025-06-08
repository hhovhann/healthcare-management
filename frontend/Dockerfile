# Stage 1: Build the Vue.js application
FROM node:20-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json first to leverage Docker's layer caching
# This ensures npm install is only rerun if dependencies change
COPY package.json package-lock.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the application source code
COPY . .

# Build the Vue.js application for production
# This command generates the static assets (HTML, CSS, JS)
# typically into a 'dist' directory
RUN npm run build

# Stage 2: Serve the built application with a lightweight server
FROM node:20-alpine AS production

# Set the working directory for the production environment
WORKDIR /app

# Install a simple static file server globally.
# 'serve' is a popular choice for serving single-page applications.
RUN npm install -g serve

# Copy the built application files from the 'build' stage.
# Vue CLI's `npm run build` typically outputs to the 'dist' directory, not '.next'.
COPY --from=build /app/dist ./dist

# It's good practice to copy package.json for production dependencies if needed,
# though for a pure static SPA served by 'serve', it's often not strictly necessary
# unless there are specific runtime node_modules dependencies.
COPY --from=build /app/package.json ./

# Install only production dependencies. This helps keep the final image size smaller.
# If 'serve' provides everything needed, this step might be optimized further,
# but it's safe to include for general cases.
RUN npm install --omit=dev

# Expose port 3000, which is the port your application will run on
EXPOSE 3000

# Command to run the application in production.
# Instead of 'npm start' (which isn't defined in your package.json),
# we use 'serve' to serve the static files from the 'dist' directory.
# '-s dist' tells serve to use 'dist' as the single-page application root.
# '-l 3000' specifies that serve should listen on port 3000.
CMD ["serve", "-s", "dist", "-l", "3000"]