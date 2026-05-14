const fs = require('node:fs');
const path = require('node:path');

const rootDir = path.resolve(__dirname, '..');
const legacyBuildDir = path.join(rootDir, 'build');
const containerBuildDir = path.join(rootDir, 'packages', 'container', 'build');
const ampOfflineBuildDir = path.join(rootDir, 'packages', 'ampoffline', 'build');

const copyDirectory = (sourceDir, targetDir) => {
  fs.mkdirSync(targetDir, { recursive: true });

  fs.readdirSync(sourceDir, { withFileTypes: true }).forEach((entry) => {
    const sourcePath = path.join(sourceDir, entry.name);
    const targetPath = path.join(targetDir, entry.name);

    if (entry.isDirectory()) {
      copyDirectory(sourcePath, targetPath);
      return;
    }

    if (entry.isSymbolicLink()) {
      fs.symlinkSync(fs.readlinkSync(sourcePath), targetPath);
      return;
    }

    fs.copyFileSync(sourcePath, targetPath);
  });
};

const syncBuildDirectory = (sourceDir, targetDir) => {
  fs.rmSync(targetDir, { recursive: true, force: true });
  copyDirectory(sourceDir, targetDir);
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