const fs = require('node:fs');
const path = require('node:path');

const rootDir = path.resolve(__dirname, '..');
const legacyBuildDir = path.join(rootDir, 'build');
const reampv2AppBuildDir = path.join(rootDir, 'packages', 'reampv2-app', 'build');
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

if (!fs.existsSync(reampv2AppBuildDir)) {
  throw new Error(`Missing required reampv2 build output: ${reampv2AppBuildDir}`);
}

// Publish the legacy top-level path from the reampv2 app bundle because AMP
// menu entries and JSP redirects still point to hash routes like
// /TEMPLATE/reampv2/build/index.html#/report_generator.
syncBuildDirectory(reampv2AppBuildDir, legacyBuildDir);
console.log(`Synced ${reampv2AppBuildDir} -> ${legacyBuildDir}`);

if (fs.existsSync(ampOfflineBuildDir)) {
  const legacyAmpOfflineDir = path.join(legacyBuildDir, 'ampoffline');
  syncBuildDirectory(ampOfflineBuildDir, legacyAmpOfflineDir);
  console.log(`Synced ${ampOfflineBuildDir} -> ${legacyAmpOfflineDir}`);
}