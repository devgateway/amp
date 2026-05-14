const fs = require('node:fs');
const path = require('node:path');

const rootDir = path.resolve(__dirname, '..');
const legacyBuildDir = path.join(rootDir, 'build');
const containerBuildDir = path.join(rootDir, 'packages', 'container', 'build');
const ampOfflineBuildDir = path.join(rootDir, 'packages', 'ampoffline', 'build');

const syncBuildDirectory = (sourceDir, targetDir) => {
  fs.rmSync(targetDir, { recursive: true, force: true });
  fs.cpSync(sourceDir, targetDir, { recursive: true, force: true });
};

if (!fs.existsSync(containerBuildDir)) {
  throw new Error(`Missing required container build output: ${containerBuildDir}`);
}

// Keep the package build locations intact, but also publish the legacy path
// still referenced by AMP menu entries and JSP redirects.
syncBuildDirectory(containerBuildDir, legacyBuildDir);
console.log(`Synced ${containerBuildDir} -> ${legacyBuildDir}`);

if (fs.existsSync(ampOfflineBuildDir)) {
  const legacyAmpOfflineDir = path.join(legacyBuildDir, 'ampoffline');
  syncBuildDirectory(ampOfflineBuildDir, legacyAmpOfflineDir);
  console.log(`Synced ${ampOfflineBuildDir} -> ${legacyAmpOfflineDir}`);
}